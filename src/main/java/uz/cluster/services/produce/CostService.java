package uz.cluster.services.produce;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.produce.CostDao;
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
@RequiredArgsConstructor
public class CostService {

    private final CostRepository costRepository;

    private final PurchaseRepository purchaseRepository;

    private final ReadyProductRepository readyProductRepository;

    private final CostTypeRepository costTypeRepository;

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_VIEW)
    public List<CostDao> getList() {
        return costRepository.findAll().stream().map(Cost::asDao).collect(Collectors.toList());
    }

    public CostDao getById(int id) {
        Optional<Cost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isEmpty()) {
            return null;
        } else {
            return optionalPrice.get().asDao();
        }
    }

    public List<DashboardLogistic> getDashboardDataMonthly() {
        List<DashboardLogistic> dashboardLogistics = new ArrayList<>();
        List<LogisticDao> list = costRepository.getAllByCostIdMonthly();//
        double totalCostAmount = 0;

        for (LogisticDao logistic : list){
            DashboardLogistic dashboardLogistic = new DashboardLogistic();
            dashboardLogistic.setCostId(logistic.getCostId());
            Optional<CostType> optionalProductType = costTypeRepository.findById(logistic.getCostId());
            optionalProductType.ifPresent(costType -> dashboardLogistic.setCostName(costType.getName().getActiveLanguage()));
            dashboardLogistic.setAmount(logistic.getAmount());
            totalCostAmount += logistic.getAmount();
            dashboardLogistics.add(dashboardLogistic);
        }

        DashboardLogistic dashboardLogisticTotal = new DashboardLogistic();
        dashboardLogisticTotal.setCostId(CostEnum.ALL_COST.getValue());
        dashboardLogisticTotal.setCostName("Umumiy xarajat");
        dashboardLogisticTotal.setAmount(totalCostAmount);
        dashboardLogistics.add(dashboardLogisticTotal);

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
        dashboardLogisticTotalProfit.setAmount(dashboardLogisticTotalIncome.getAmount() - dashboardLogisticTotal.getAmount());
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
        List<LogisticDao> list = costRepository.getAllByCostIdDaily();//
        double totalCostAmount = 0;

        for (LogisticDao logistic : list){
            DashboardLogistic dashboardLogistic = new DashboardLogistic();
            dashboardLogistic.setCostId(logistic.getCostId());
            Optional<CostType> optionalProductType = costTypeRepository.findById(logistic.getCostId());
            optionalProductType.ifPresent(costType -> dashboardLogistic.setCostName(costType.getName().getActiveLanguage()));
            dashboardLogistic.setAmount(logistic.getAmount());
            totalCostAmount += logistic.getAmount();
            dashboardLogistics.add(dashboardLogistic);
        }

        DashboardLogistic dashboardLogisticTotal = new DashboardLogistic();
        dashboardLogisticTotal.setCostId(CostEnum.ALL_COST.getValue());
        dashboardLogisticTotal.setCostName("Umumiy xarajat");
        dashboardLogisticTotal.setAmount(totalCostAmount);
        dashboardLogistics.add(dashboardLogisticTotal);

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
        dashboardLogisticTotalProfit.setAmount(dashboardLogisticTotalIncome.getAmount() - dashboardLogisticTotal.getAmount());
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

        if (price.getCostTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<CostType> optionalProductType = costTypeRepository.findById(price.getCostTypeId());
        optionalProductType.ifPresent(price::setCostType);

        if (price.getId() != 0) {
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
            return new ApiResponse(true, priceEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.PRODUCE_COST, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Cost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isPresent()){
            costRepository.deleteById(id);
            return new ApiResponse(true,LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
