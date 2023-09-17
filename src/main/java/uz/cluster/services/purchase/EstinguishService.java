package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.entity.purchase.Estinguish;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.EstinguishRepository;
import uz.cluster.repository.purchase.PurchaseRepository;   
import uz.cluster.util.LanguageManager;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstinguishService {

    private final EstinguishRepository estinguishRepository;

    private final PurchaseRepository purchaseRepository;

    @CheckPermission(form = FormEnum.ESTINGUISH_DEBT, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(Estinguish estinguish) {

        if (estinguish.getPurchases().size() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        double totalPaidValue = estinguish.getPaidValue();
        for (Purchase purchase : estinguish.getPurchases()){
            Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchase.getId());
            if (optionalPurchase.isPresent()){
                if (estinguish.getDebtTotalValue() == estinguish.getPaidValue()){
                    optionalPurchase.get().setDebtTotalValue(0);
                    optionalPurchase.get().setPaidTotalValue(optionalPurchase.get().getTotalValue());
                    purchaseRepository.save(optionalPurchase.get());
                }else{
                    if (totalPaidValue - optionalPurchase.get().getDebtTotalValue() > 0){
                        totalPaidValue = totalPaidValue - optionalPurchase.get().getDebtTotalValue();
                        optionalPurchase.get().setDebtTotalValue(0);
                        optionalPurchase.get().setPaidTotalValue(optionalPurchase.get().getTotalValue());
                        purchaseRepository.save(optionalPurchase.get());
                    }else{
                        optionalPurchase.get().setDebtTotalValue(optionalPurchase.get().getDebtTotalValue() - totalPaidValue);
                        optionalPurchase.get().setPaidTotalValue(totalPaidValue);
                        totalPaidValue = totalPaidValue - optionalPurchase.get().getDebtTotalValue();
                        purchaseRepository.save(optionalPurchase.get());
                    }
                }
            }
        }

        Estinguish saved = estinguishRepository.save(estinguish);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }
}
