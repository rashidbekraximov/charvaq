package uz.cluster.services.produce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.references.model.CostType;
import uz.cluster.enums.CostEnum;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.produce.ProduceEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.LogisticDao;
import uz.cluster.repository.produce.CostRepository;
import uz.cluster.repository.produce.ProduceBarDao;
import uz.cluster.repository.produce.ReadyProductRepository;
import uz.cluster.repository.purchase.PurchaseDto;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.repository.references.CostTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CostService {

    private final CostRepository costRepository;

    private final PurchaseRepository purchaseRepository;

    private final ReadyProductRepository readyProductRepository;

    private final CostTypeRepository costTypeRepository;

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_VIEW)
    public List<CostDao> getList() {
        List<CostDao> costs = costRepository.findAll().stream().map(Cost::asDao).collect(Collectors.toList());
        List<CostDao> filtered = new ArrayList<>();
        for (CostDao cost : costs){
            if (cost.getCostType() != null){
                filtered.add(cost);
            }
        }
        return filtered;
    }

    public CostDao getById(int id) {
        Optional<Cost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isEmpty()) {
            log.error("Id " + id + " topilmadi");
            return null;
        } else {
            log.info("ID " + id + " haqida ma'lumot ekranga chiqarildi :)");
            return optionalPrice.get().asDao();
        }
    }

    public List<DashboardLogistic> getDashboardDataMonthly() {
        List<DashboardLogistic> dashboardLogistics = new ArrayList<>();
        List<ProduceBarDao> list = costRepository.getAllByCostIdMonthly();//
        double totalRealAmount = 0;
        double totalAmount = 0;
        double totalPermamentAmount = 0;

        for (ProduceBarDao logistic : list){
            if (ProduceEnum.PERMAMENT_COST.getValue() == logistic.getSpendingTypeId()){
                totalPermamentAmount += logistic.getAmount();
            }else {
                totalRealAmount += logistic.getAmount();
            }
            totalAmount = totalPermamentAmount + totalRealAmount;
        }

        DashboardLogistic dashboardLogisticTotal = new DashboardLogistic();
        dashboardLogisticTotal.setCostId(CostEnum.REAL_COST.getValue());
        dashboardLogisticTotal.setCostName("Doimiy xarajat");
        dashboardLogisticTotal.setAmount(totalPermamentAmount);
        dashboardLogistics.add(dashboardLogisticTotal);

        DashboardLogistic dashboardReal = new DashboardLogistic();
        dashboardReal.setCostId(CostEnum.ALL_COST.getValue());
        dashboardReal.setCostName("Real xarajat");
        dashboardReal.setAmount(totalRealAmount);
        dashboardLogistics.add(dashboardReal);

        DashboardLogistic dashboardLogisticTotalPurchase = new DashboardLogistic();
        DashboardLogistic dashboardLogisticTotalIncome = new DashboardLogistic();
        List<PurchaseDto> allIncome = purchaseRepository.getAllIncomeProductTypeIdMonthly(PurchaseEnum.SH_BLOK.getValue());//
        dashboardLogisticTotalIncome.setCostId(CostEnum.ALL_INCOME.getValue());
        dashboardLogisticTotalIncome.setCostName("Daromad");
        allIncome.forEach(last -> {
            dashboardLogisticTotalIncome.setAmount(last.getAllAmount());
            dashboardLogisticTotalPurchase.setAmount(last.getWeight());
        });
        dashboardLogistics.add(dashboardLogisticTotalIncome);

        DashboardLogistic dashboardLogisticTotalProfit = new DashboardLogistic();
        dashboardLogisticTotalProfit.setCostId(CostEnum.ALL_PROFIT.getValue());
        dashboardLogisticTotalProfit.setAmount(dashboardLogisticTotalIncome.getAmount() - totalAmount);
        dashboardLogisticTotalProfit.setCostName("Foyda");
        dashboardLogistics.add(dashboardLogisticTotalProfit);

        dashboardLogisticTotalPurchase.setCostName("Sotilgan mahsulot");
        dashboardLogisticTotalPurchase.setCostId(2000);
        dashboardLogistics.add(dashboardLogisticTotalPurchase);

        DashboardLogistic dashboardLogisticTotalAmount = new DashboardLogistic();
        List<Double> amount = readyProductRepository.getAllAmountByProductTypeId(PurchaseEnum.SH_BLOK.getValue());//
        amount.forEach(a -> {
            dashboardLogisticTotalAmount.setAmount(dashboardLogisticTotalAmount.getAmount() + a);
        });
        dashboardLogisticTotalAmount.setCostName("Mahsulot qoldig'i");
        dashboardLogisticTotalAmount.setCostId(1000);
        dashboardLogistics.add(dashboardLogisticTotalAmount);

        return IntStream.range(0, dashboardLogistics.size()).map(i -> dashboardLogistics.size() - 1-i).mapToObj(dashboardLogistics::get).collect(Collectors.toList());
    }


    public List<DashboardLogistic> getDashboardDataDaily() {
        List<DashboardLogistic> dashboardLogistics = new ArrayList<>();
        List<ProduceBarDao> list = costRepository.getAllByCostIdDaily();//
        double totalRealAmount = 0;
        double totalAmount = 0;
        double totalPermamentAmount = 0;

        for (ProduceBarDao logistic : list){
            if (ProduceEnum.PERMAMENT_COST.getValue() == logistic.getSpendingTypeId()){
                totalPermamentAmount += logistic.getAmount();
            }else {
                totalRealAmount += logistic.getAmount();
            }
            totalAmount = totalPermamentAmount + totalRealAmount;
        }

        DashboardLogistic dashboardLogisticTotal = new DashboardLogistic();
        dashboardLogisticTotal.setCostId(CostEnum.REAL_COST.getValue());
        dashboardLogisticTotal.setCostName("Doimiy xarajat");
        dashboardLogisticTotal.setAmount(totalPermamentAmount);
        dashboardLogistics.add(dashboardLogisticTotal);

        DashboardLogistic dashboardReal = new DashboardLogistic();
        dashboardReal.setCostId(CostEnum.ALL_COST.getValue());
        dashboardReal.setCostName("Real xarajat");
        dashboardReal.setAmount(totalRealAmount);
        dashboardLogistics.add(dashboardReal);

        DashboardLogistic dashboardLogisticTotalPurchase = new DashboardLogistic();
        DashboardLogistic dashboardLogisticTotalIncome = new DashboardLogistic();
        List<PurchaseDto> allIncome = purchaseRepository.getAllIncomeProductTypeIdDaily(PurchaseEnum.SH_BLOK.getValue());//
        dashboardLogisticTotalIncome.setCostId(CostEnum.ALL_INCOME.getValue());
        dashboardLogisticTotalIncome.setCostName("Daromad");
        allIncome.forEach(last -> {
            dashboardLogisticTotalIncome.setAmount(last.getAllAmount());
            dashboardLogisticTotalPurchase.setAmount(last.getWeight());
        });
        dashboardLogistics.add(dashboardLogisticTotalIncome);

        DashboardLogistic dashboardLogisticTotalProfit = new DashboardLogistic();
        dashboardLogisticTotalProfit.setCostId(CostEnum.ALL_PROFIT.getValue());
        dashboardLogisticTotalProfit.setAmount(dashboardLogisticTotalIncome.getAmount() - totalAmount);
        dashboardLogisticTotalProfit.setCostName("Foyda");
        dashboardLogistics.add(dashboardLogisticTotalProfit);

        dashboardLogisticTotalPurchase.setCostName("Sotilgan mahsulot");
        dashboardLogisticTotalPurchase.setCostId(2000);
        dashboardLogistics.add(dashboardLogisticTotalPurchase);

        DashboardLogistic dashboardLogisticTotalAmount = new DashboardLogistic();
        List<Double> amount = readyProductRepository.getAllAmountByProductTypeId(PurchaseEnum.SH_BLOK.getValue());//
        amount.forEach(a -> {
            dashboardLogisticTotalAmount.setAmount(dashboardLogisticTotalAmount.getAmount() + a);
        });
        dashboardLogisticTotalAmount.setCostName("Mahsulot qoldig'i");
        dashboardLogisticTotalAmount.setCostId(1000);
        dashboardLogistics.add(dashboardLogisticTotalAmount);

        return IntStream.range(0, dashboardLogistics.size()).map(i -> dashboardLogistics.size() - 1-i).mapToObj(dashboardLogistics::get).collect(Collectors.toList());
    }

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(CostDao priceDao) {
        Cost price = priceDao.copy(priceDao);

        Optional<CostType> optionalProductType = costTypeRepository.findById(price.getCostTypeId());
        optionalProductType.ifPresent(price::setCostType);
        price.setSpendingTypeId(ProduceEnum.REAL_COST.getValue());
        if (price.getId() != 0) {
            log.info("Ma'lumot yangilandi ");
            return edit(price);
        }

        Cost priceSaved = costRepository.save(price);
        return new ApiResponse(true, priceSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Cost cost) {
        Optional<Cost> optionalPrice = costRepository.findById(cost.getId());
        if (optionalPrice.isPresent()){
            Cost priceEdited = costRepository.save(cost);
            log.info("Ma'lumot muvaffaqiyatli yangilandi !");
            return new ApiResponse(true, priceEdited, LanguageManager.getLangMessage("edited"));
        }else{
            log.error("Malumot topilmadi !");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Cost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isPresent()){
            costRepository.deleteById(id);
            log.info("Bu id " + id + " muvaffaqiyatli o'chirildi !");
            return new ApiResponse(true,LanguageManager.getLangMessage("deleted"));
        }else{
            log.error("Bu Id " + id + " o'chirilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
