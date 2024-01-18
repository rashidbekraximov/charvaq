package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.entity.purchase.DailyIncome;
import uz.cluster.entity.purchase.Estinguish;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.enums.Status;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.DailyIncomeRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyIncomeService {

    private final DailyIncomeRepository dailyIncomeRepository;

    //    @CheckPermission(form = FormEnum.DAILY_COST, permission = Action.CAN_VIEW)
    public List<DailyIncome> getDailyIncomeList() {
        return dailyIncomeRepository.findAllByOrderByStatusAsc();
    }

//    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_VIEW)
    public ApiResponse confirm(long id) {
        Optional<DailyIncome> optional = dailyIncomeRepository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
        optional.get().setStatus(Status.PASSIVE);
        dailyIncomeRepository.save(optional.get());
        return new ApiResponse(true, null, LanguageManager.getLangMessage("saved"));
    }

    @Transactional
    public void addFromPurchase(Purchase purchase) {
        Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(purchase.getDate());
        if (optional.isPresent()) {
            optional.get().setIncome(optional.get().getIncome() + purchase.getPaidTotalValue());
            dailyIncomeRepository.save(optional.get());
        }else{
            DailyIncome dailyIncome = new DailyIncome();
            dailyIncome.setDate(purchase.getDate());
            dailyIncome.setIncome(purchase.getPaidTotalValue());
            dailyIncome.setStatus(Status.ACTIVE);
            dailyIncomeRepository.save(dailyIncome);
        }
    }

    @Transactional
    public void addFromEstinguish(Estinguish purchaseDao) {
        Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(purchaseDao.getDate());
        if (optional.isPresent() && purchaseDao.getRemainderDebtValue() == 0) {
            optional.get().setIncome(optional.get().getIncome() + purchaseDao.getPaidValue());
            dailyIncomeRepository.save(optional.get());
        } else {
            DailyIncome dailyIncome = new DailyIncome();
            dailyIncome.setDate(purchaseDao.getDate());
            dailyIncome.setIncome(purchaseDao.getPaidValue());
            dailyIncome.setStatus(Status.ACTIVE);
            dailyIncomeRepository.save(dailyIncome);
        }
    }
}
