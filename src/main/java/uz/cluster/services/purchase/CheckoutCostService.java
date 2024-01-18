package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
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
import uz.cluster.util.LanguageManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutCostService {

    private final CheckoutCostRepository costRepository;

    private final DailyIncomeRepository dailyIncomeRepository;

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<CheckoutCost> getList() {
        return costRepository.findAll();
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<CheckoutCost> getDailyList(LocalDate date) {
        return costRepository.findAllByDate(date);
    }

    public CheckoutCost getById(long id) {
        Optional<CheckoutCost> optional = costRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        } else {
            return optional.get();
        }
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(CheckoutCost price) {
        Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(price.getDate());
        if (optional.isPresent() && optional.get().getStatus() == Status.PASSIVE){
            return new ApiResponse(false,LanguageManager.getLangMessage("confirmed_or_deleted"));
        }

        if (price.getId() != 0){
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
                return new ApiResponse(true,LanguageManager.getLangMessage("edited"));
            }else{
                return new ApiResponse(false,LanguageManager.getLangMessage("confirmed_or_deleted"));
            }
        }else{
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
                return new ApiResponse(true,LanguageManager.getLangMessage("deleted"));
            }else{
                return new ApiResponse(true,LanguageManager.getLangMessage("confirmed_or_deleted"));
            }
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
