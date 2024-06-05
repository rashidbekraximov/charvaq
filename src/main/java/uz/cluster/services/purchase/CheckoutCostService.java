package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.logistic.DrobilkaDao;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.entity.logistic.Drobilka;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.purchase.CheckoutCost;
import uz.cluster.entity.purchase.DailyIncome;
import uz.cluster.entity.references.model.CostType;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.enums.CostEnum;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.LogisticDao;
import uz.cluster.repository.produce.CostRepository;
import uz.cluster.repository.produce.ReadyProductRepository;
import uz.cluster.repository.purchase.CheckoutCostRepository;
import uz.cluster.repository.purchase.DailyIncomeRepository;
import uz.cluster.repository.purchase.PurchaseDto;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.repository.references.CostTypeRepository;
import uz.cluster.repository.references.DirectionRepository;
import uz.cluster.services.produce.ProduceRemainderService;
import uz.cluster.util.LanguageManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutCostService {

    private final CheckoutCostRepository costRepository;

    private final DailyIncomeRepository dailyIncomeRepository;

    private final CostTypeRepository costTypeRepository;

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<CheckoutCost> getList(int id) {
        if (id != 0){
            return costRepository.findAllByCostType_Id(id);
        }else {
            return costRepository.findAll();
        }
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<CheckoutCost> getDailyList(LocalDate date) {
        return costRepository.findAllByDate(date);
    }

    public CheckoutCost getById(long id) {
        Optional<CheckoutCost> optional = costRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("Id " + id + " topilmadi");
            return null;
        } else {
            log.info("ID " + id + "haqida ma'lumot ekranga chiqarildi :)");
            return optional.get();
        }
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(CheckoutCost price) {
        Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(price.getDate());
        if (optional.isPresent() && optional.get().getStatus() == Status.PASSIVE){
            log.error("Check chiqarilmadi !");
            return new ApiResponse(false,LanguageManager.getLangMessage("confirmed_or_deleted"));
        }

        if (price.getCostTypeId() == 0){
            log.error("Check chiqarib bo'lmadi !");
            return new ApiResponse(false,LanguageManager.getLangMessage("confirmed_or_deleted"));
        }

        Optional<CostType> optionalProductType = costTypeRepository.findById(price.getCostTypeId());
        optionalProductType.ifPresent(price::setCostType);


        if (price.getId() != 0){
            log.info("Ma'lumot yangilandi ");
            return edit(price);
        }

        if (optional.isPresent()){
            optional.get().setSpending(optional.get().getSpending() + price.getAmount());
            dailyIncomeRepository.save(optional.get());
        }else{
            DailyIncome daily = new DailyIncome();
            daily.setDate(price.getDate());
            daily.setIncome(0);
            daily.setSpending(price.getAmount());
            daily.setStatus(Status.ACTIVE);
            dailyIncomeRepository.save(daily);
        }
        CheckoutCost priceSaved = costRepository.save(price);
        log.info("Ma'lumot muvaffaqiyatli saqlandi !");
        return new ApiResponse(true, priceSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(CheckoutCost checkoutCost) {
        Optional<CheckoutCost> optionalPrice = costRepository.findById(checkoutCost.getId());
        if (optionalPrice.isPresent()){
            Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(optionalPrice.get().getDate());
            if (optional.isPresent() && optional.get().getStatus() == Status.ACTIVE){
                optional.get().setSpending(optional.get().getSpending() - optionalPrice.get().getAmount() + checkoutCost.getAmount());
                dailyIncomeRepository.save(optional.get());
                costRepository.save(checkoutCost);
                log.info("Ma'lumot muvaffaqiyatli yangilandi !");
                return new ApiResponse(true,LanguageManager.getLangMessage("edited"));
            }else{
                log.error("Ma'lumotni yangilab bo'lmadi !");
                return new ApiResponse(false,LanguageManager.getLangMessage("confirmed_or_deleted"));
            }
        }else{
            log.error("Malumot topilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }


    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        Optional<CheckoutCost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isPresent()){
            Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(optionalPrice.get().getDate());
            if (optional.isPresent() && optional.get().getStatus() == Status.ACTIVE){
                optional.get().setSpending(optional.get().getSpending() - optionalPrice.get().getAmount());
                dailyIncomeRepository.save(optional.get());
                costRepository.deleteById(id);
                log.info("Bu id " + id + " muvaffaqiyatli o'chirildi !");
                return new ApiResponse(true,LanguageManager.getLangMessage("deleted"));
            }else{
                log.error("Ma'lumotni o'chirib bo'lmadi !");
                return new ApiResponse(true,LanguageManager.getLangMessage("confirmed_or_deleted"));
            }
        }else{
            log.error("Bu Id " + id + " o'chirilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
