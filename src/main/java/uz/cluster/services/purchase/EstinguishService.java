package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.AllDebtDao;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.purchase.Estinguish;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.entity.purchase.PurchasedProduct;
import uz.cluster.entity.references.model.PaymentType;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.PaymentEnum;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.LBPurchaseRepository;
import uz.cluster.repository.purchase.EstinguishRepository;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.repository.purchase.PurchasedProductRepository;
import uz.cluster.repository.references.PaymentTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstinguishService {

    private final EstinguishRepository estinguishRepository;

    private final PurchaseRepository purchaseRepository;

    private final PurchasedProductRepository purchasedProductRepository;

    private final LBPurchaseRepository lbPurchaseRepository;

    private final DailyIncomeService dailyIncomeService;

    private final PaymentTypeRepository paymentTypeRepository;

    @CheckPermission(form = FormEnum.ESTINGUISH_DEBT, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(Estinguish estinguish) {
        if (estinguish.getAllDebts().size() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        double totalPaidValue = estinguish.getPaidValue();
        PaymentType paymentType = paymentTypeRepository.findById(PaymentEnum.NAQD.getValue()).orElse(null);
        for (AllDebtDao purchase : estinguish.getAllDebts()){
            if (purchase.getMchj() == MCHJ.CHSM){
                dailyIncomeService.addFromEstinguish(estinguish);
                Optional<Purchase> optionalPurchase = purchaseRepository.findById((int) purchase.getId());
                if (optionalPurchase.isPresent()){
                    if (estinguish.getDebtTotalValue() == estinguish.getPaidValue()){
                        optionalPurchase.get().setDebtTotalValue(0);
                        optionalPurchase.get().setPaymentType(paymentType);
                        optionalPurchase.get().setPaidDate(estinguish.getDate());
                        optionalPurchase.get().setPaidTotalValue(optionalPurchase.get().getTotalValue());
                        purchaseRepository.save(optionalPurchase.get());
                    }else{
                        if (totalPaidValue - optionalPurchase.get().getDebtTotalValue() > 0){
                            totalPaidValue = totalPaidValue - optionalPurchase.get().getDebtTotalValue();
                            optionalPurchase.get().setDebtTotalValue(0);
                            optionalPurchase.get().setPaymentType(paymentType);
                            optionalPurchase.get().setPaidDate(estinguish.getDate());
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
            }else{
                Optional<LBPurchase> optionalPurchase = lbPurchaseRepository.findById(purchase.getId());
                if (optionalPurchase.isPresent()){
                    if (estinguish.getDebtTotalValue() == estinguish.getPaidValue()){
                        optionalPurchase.get().setDebtTotalValue(0);
                        optionalPurchase.get().setGivenValue(optionalPurchase.get().getTotalValue());
                        lbPurchaseRepository.save(optionalPurchase.get());
                    }else{
                        if (totalPaidValue - optionalPurchase.get().getDebtTotalValue() > 0){
                            totalPaidValue = totalPaidValue - optionalPurchase.get().getDebtTotalValue();
                            optionalPurchase.get().setDebtTotalValue(0);
                            optionalPurchase.get().setGivenValue(optionalPurchase.get().getTotalValue());
                            lbPurchaseRepository.save(optionalPurchase.get());
                        }else{
                            optionalPurchase.get().setDebtTotalValue(optionalPurchase.get().getDebtTotalValue() - totalPaidValue);
                            optionalPurchase.get().setGivenValue(totalPaidValue);
                            totalPaidValue = totalPaidValue - optionalPurchase.get().getDebtTotalValue();
                            lbPurchaseRepository.save(optionalPurchase.get());
                        }
                    }
                }
            }
        }

        Estinguish saved = estinguishRepository.save(estinguish);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }


    @CheckPermission(form = FormEnum.ESTINGUISH_DEBT, permission = Action.CAN_ADD)
    public List<AllDebtDao> getDebtList() {
        List<AllDebtDao> allDebts = new ArrayList<>();
        List<Purchase> purchases = purchaseRepository.getAllDebts();
        List<LBPurchase> lbPurchases = lbPurchaseRepository.getAllLBDebts();
        int key = 0;
        for (Purchase purchase : purchases) {
            List<PurchasedProduct> purchasedProducts = purchasedProductRepository.findAllByPurchaseIdOrderByProductType(purchase.getId());
            AllDebtDao allDebtDao = new AllDebtDao();
            key++;
            allDebtDao.setKey(key);
            allDebtDao.setId(purchase.getId());
            allDebtDao.setDate(purchase.getDate());
            allDebtDao.setClient(purchase.getClient());
            allDebtDao.setPaymentType(purchase.getPaymentType().getName().getUz_lat());
            allDebtDao.setTotalValue(purchase.getTotalValue());
            allDebtDao.setDebtTotalValue(purchase.getDebtTotalValue());
            allDebtDao.setMchj(MCHJ.CHSM);
            StringBuilder txt = new StringBuilder();
            for (PurchasedProduct p : purchasedProducts) {
                txt.append(p.getProductType().getName().getActiveLanguage()).append(",");
            }
            allDebtDao.setProductsText(txt.substring(0,txt.length()-1));
            allDebtDao.setMchj(MCHJ.CHSM);
            allDebts.add(allDebtDao);
        }
        for (LBPurchase lbPurchase : lbPurchases) {
            key++;
            AllDebtDao allDebtDao = new AllDebtDao();
            allDebtDao.setKey(key);
            allDebtDao.setId(lbPurchase.getId());
            allDebtDao.setDate(lbPurchase.getDate());
            allDebtDao.setClient(lbPurchase.getCustomer());
            allDebtDao.setTotalValue(lbPurchase.getTotalValue());
            allDebtDao.setDebtTotalValue(lbPurchase.getDebtTotalValue());
            allDebtDao.setProductsText("");
            allDebtDao.setPaymentType("");
            allDebtDao.setMchj(MCHJ.LEADER_BETON_1);
            allDebts.add(allDebtDao);
        }
        return allDebts;
    }

    @CheckPermission(form = FormEnum.ESTINGUISH_DEBT, permission = Action.CAN_ADD)
    public List<AllDebtDao> getSearchList(String client) {
        List<AllDebtDao> allDebts = new ArrayList<>();
        if (!client.isEmpty() && !client.trim().isEmpty()){
            List<Purchase> purchases = purchaseRepository.getSearchedDebts(client.toLowerCase());
            List<LBPurchase> lbPurchases = lbPurchaseRepository.getSearchedLBDebts(client.toLowerCase());
            int key = 0;
            for (Purchase purchase : purchases) {
                AllDebtDao allDebtDao = new AllDebtDao();
                key++;
                allDebtDao.setKey(key);
                allDebtDao.setId(purchase.getId());
                allDebtDao.setDate(purchase.getDate());
                allDebtDao.setClient(purchase.getClient());
                allDebtDao.setTotalValue(purchase.getTotalValue());
                allDebtDao.setDebtTotalValue(purchase.getDebtTotalValue());
                allDebtDao.setMchj(MCHJ.CHSM);
                allDebts.add(allDebtDao);
            }
            for (LBPurchase lbPurchase : lbPurchases) {
                AllDebtDao allDebtDao = new AllDebtDao();
                key++;
                allDebtDao.setKey(key);
                allDebtDao.setId(lbPurchase.getId());
                allDebtDao.setDate(lbPurchase.getDate());
                allDebtDao.setClient(lbPurchase.getCustomer());
                allDebtDao.setTotalValue(lbPurchase.getTotalValue());
                allDebtDao.setDebtTotalValue(lbPurchase.getDebtTotalValue());
                allDebtDao.setMchj(MCHJ.LEADER_BETON_1);
                allDebts.add(allDebtDao);
            }
            return allDebts;
        }else{
            List<Purchase> purchases = purchaseRepository.getAllDebts();
            List<LBPurchase> lbPurchases = lbPurchaseRepository.getAllLBDebts();
            int key = 0;
            for (Purchase purchase : purchases) {
                AllDebtDao allDebtDao = new AllDebtDao();
                key++;
                allDebtDao.setKey(key);
                allDebtDao.setId(purchase.getId());
                allDebtDao.setDate(purchase.getDate());
                allDebtDao.setClient(purchase.getClient());
                allDebtDao.setTotalValue(purchase.getTotalValue());
                allDebtDao.setDebtTotalValue(purchase.getDebtTotalValue());
                allDebtDao.setMchj(MCHJ.CHSM);
                allDebts.add(allDebtDao);
            }
            for (LBPurchase lbPurchase : lbPurchases) {
                AllDebtDao allDebtDao = new AllDebtDao();
                key++;
                allDebtDao.setKey(key);
                allDebtDao.setId(lbPurchase.getId());
                allDebtDao.setDate(lbPurchase.getDate());
                allDebtDao.setClient(lbPurchase.getCustomer());
                allDebtDao.setTotalValue(lbPurchase.getTotalValue());
                allDebtDao.setDebtTotalValue(lbPurchase.getDebtTotalValue());
                allDebtDao.setMchj(MCHJ.LEADER_BETON_1);
                allDebts.add(allDebtDao);
            }
            return allDebts;
        }
    }


}
