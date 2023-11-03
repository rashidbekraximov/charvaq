package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.produce.CostDao;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutCostService {

    private final CheckoutCostRepository costRepository;

    private final DirectionRepository directionRepository;

    private final CostTypeRepository costTypeRepository;

    private final DailyIncomeRepository dailyIncomeRepository;

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<CheckoutCost> getList() {
        return costRepository.findAll();
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(CheckoutCost price) {

        if (price.getCostTypeId() == 0 || price.getDirectionId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<CostType> optionalProductType = costTypeRepository.findById(price.getCostTypeId());
        optionalProductType.ifPresent(price::setCostType);

        Optional<Direction> optionalDirection = directionRepository.findById(price.getDirectionId());
        optionalDirection.ifPresent(price::setDirection);

        Optional<DailyIncome> optional = dailyIncomeRepository.findByDateAndProductId(price.getDate(),(price.getDirectionId() == 2 ? 2 : 1));
        if (optional.isPresent() && optional.get().getStatus() == Status.PASSIVE){
            return new ApiResponse(true,LanguageManager.getLangMessage("confirmed_or_deleted"));
        }
        if (optional.isPresent()){
            optional.get().setSpending(optional.get().getSpending() + price.getAmount());
            dailyIncomeRepository.save(optional.get());
        }else{
            DailyIncome daily = new DailyIncome();
            daily.setDate(price.getDate());
            daily.setIncome(0);
            if (price.getDirectionId() == 1){
                daily.setProductId(2);
            }else{
                daily.setProductId(1);
            }
            daily.setSpending(price.getAmount());
            daily.setStatus(Status.ACTIVE);
            dailyIncomeRepository.save(daily);
        }
        CheckoutCost priceSaved = costRepository.save(price);
        return new ApiResponse(true, priceSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        Optional<CheckoutCost> optionalPrice = costRepository.findById(id);
        if (optionalPrice.isPresent()){
            Optional<DailyIncome> optional = dailyIncomeRepository.findByDateAndProductId(optionalPrice.get().getDate(),(optionalPrice.get().getDirectionId() == 2 ? 2 : 1));
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
