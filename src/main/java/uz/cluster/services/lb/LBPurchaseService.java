package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.IngredientDao;
import uz.cluster.dao.lb.LBPurchaseDao;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.entity.lb.Ingredient;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.LBPurchaseRepository;
import uz.cluster.repository.lb.MixerRepository;
import uz.cluster.repository.purchase.RemainderRepository;
import uz.cluster.services.purchase.PurchaseService;
import uz.cluster.util.LanguageManager;
import uz.cluster.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LBPurchaseService {

    private final EntityManager entityManager;

    private final LBPurchaseRepository lbPurchaseRepository;

    private final MixerRepository mixerRepository;

    private final RemainderRepository remainderRepository;

    private final PurchaseService purchaseService;

    private final IngredientService ingredientService;

    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_VIEW)
    public List<LBPurchaseDao> getList() {
        return lbPurchaseRepository.findAll().stream().map(LBPurchase::asDao).collect(Collectors.toList());
    }

    public LBPurchaseDao getById(int id) {
        Optional<LBPurchase> optional = lbPurchaseRepository.findById((long) id);
        if (optional.isEmpty()) {
            return null;
        } else {
            return optional.get().asDao();
        }
    }
    public ApiResponse getByAmountAndMark(int mark, double amount) {
        IngredientDao lbPurchase = ingredientService.getByAmountAndMark(mark, amount);
        Optional<Remainder> remainder1 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SEMENT.getValue(), MCHJ.LB);
        boolean test = false;
        if (remainder1.isPresent() && remainder1.get().getAmount() - lbPurchase.getSement() >= 0){
            test = true;
        }
        Optional<Remainder> remainder2 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.KLINES.getValue(), MCHJ.LB);
        if (remainder2.isPresent() && remainder2.get().getAmount() - lbPurchase.getKlines() >= 0){
            test = true;
        }

        Optional<Remainder> remainder3 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SHEBEN.getValue(), MCHJ.LB);
        if (remainder3.isPresent() && remainder3.get().getAmount() - lbPurchase.getSheben() >= 0){
            test = true;
        }

        Optional<Remainder> remainder4 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.CHINOZ.getValue(), MCHJ.LB);
        if (remainder4.isPresent() && remainder4.get().getAmount() - lbPurchase.getPesok() >= 0){
            test = true;
        }

        Optional<Remainder> remainder5 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.DOBAVKA.getValue(), MCHJ.LB);
        if (remainder5.isPresent() && remainder5.get().getAmount() - lbPurchase.getDobavka() >= 0){
            test = true;
        }

        if (test){
            return new ApiResponse(true, LanguageManager.getLangMessage("enough"));
        }else {
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }
    }
    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(LBPurchaseDao lbPurchaseDao) {
        LBPurchase lbPurchase = lbPurchaseDao.copy(lbPurchaseDao);

        if (lbPurchase.getMixerId() == 0){
            return new ApiResponse(true, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<Mixer> optional =  mixerRepository.findById(lbPurchase.getMixerId());
        optional.ifPresent(lbPurchase::setMixer);

        if (lbPurchase.getId() != 0) {
            return edit(lbPurchase);
        }

        Optional<Remainder> remainder1 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SEMENT.getValue(), MCHJ.LB);
        if (remainder1.isPresent() && remainder1.get().getAmount() - lbPurchase.getSement() >= 0){
            remainder1.get().setAmount(remainder1.get().getAmount() - lbPurchase.getSement());
            remainderRepository.save(remainder1.get());
        }else{
            return new ApiResponse(true, LanguageManager.getLangMessage("remainder") + LanguageManager.getLangMessage("sement") + LanguageManager.getLangMessage("not_found"));
        }

        Optional<Remainder> remainder2 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.KLINES.getValue(), MCHJ.LB);
        if (remainder2.isPresent() && remainder2.get().getAmount() - lbPurchase.getKlines() >= 0){
            remainder2.get().setAmount(remainder2.get().getAmount() - lbPurchase.getKlines());
            remainderRepository.save(remainder2.get());
        }else{
            return new ApiResponse(true, LanguageManager.getLangMessage("remainder") + LanguageManager.getLangMessage("klines") + LanguageManager.getLangMessage("not_found"));
        }

        Optional<Remainder> remainder3 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SHEBEN.getValue(), MCHJ.LB);
        if (remainder3.isPresent() && remainder3.get().getAmount() - lbPurchase.getSheben() >= 0){
            remainder3.get().setAmount(remainder3.get().getAmount() - lbPurchase.getSheben());
            remainderRepository.save(remainder3.get());
        }else{
            return new ApiResponse(true, LanguageManager.getLangMessage("remainder") + LanguageManager.getLangMessage("sheben") + LanguageManager.getLangMessage("not_found"));
        }

        Optional<Remainder> remainder4 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.CHINOZ.getValue(), MCHJ.LB);
        if (remainder4.isPresent() && remainder4.get().getAmount() - lbPurchase.getPesok() >= 0){
            remainder4.get().setAmount(remainder4.get().getAmount() - lbPurchase.getPesok());
            remainderRepository.save(remainder4.get());
        }else{
            return new ApiResponse(true, LanguageManager.getLangMessage("remainder") + LanguageManager.getLangMessage("pesok") + LanguageManager.getLangMessage("not_found"));
        }

        Optional<Remainder> remainder5 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.DOBAVKA.getValue(), MCHJ.LB);
        if (remainder5.isPresent() && remainder5.get().getAmount() - lbPurchase.getDobavka() >= 0){
            remainder5.get().setAmount(remainder5.get().getAmount() - lbPurchase.getDobavka());
            remainderRepository.save(remainder5.get());
        }else{
            return new ApiResponse(true, LanguageManager.getLangMessage("remainder") + LanguageManager.getLangMessage("dobavka") + LanguageManager.getLangMessage("not_found"));
        }

        LBPurchase saved = lbPurchaseRepository.save(lbPurchase);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(LBPurchase lbPurchase) {
        Optional<LBPurchase> optional = lbPurchaseRepository.findById(lbPurchase.getId());

        if (optional.isPresent()){
            LBPurchase edited = lbPurchaseRepository.save(lbPurchase);
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        Optional<LBPurchase> optional = lbPurchaseRepository.findById(id);
        if (optional.isPresent()){
            lbPurchaseRepository.deleteById(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_VIEW)
    public List<LBPurchase> getPurchasesByPage(DocumentFilter documentFilter) {
        if (documentFilter == null) {
            documentFilter = new DocumentFilter();
        }

        String where = "";

        if (StringUtil.isNotEmpty(documentFilter.getClient())) {
            where += " and customer like  '%'||:customer||'%' ";
        }

        if (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null) {
            where += " and date between :beginDate and :endDate";
        }else{
            if (documentFilter.getId() != 0 || StringUtil.isNotEmpty(documentFilter.getClient()) || (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null)){
                where += " and date between :beginDate and :endDate ";
            }else{
                where += "date between :beginDate and :endDate ";
            }
        }

        if (documentFilter.getMark() != 0) {
            where += " and mark = :mark";
        }

        if (documentFilter.getAmount() != 0) {
            where += " and amount = :amount";
        }
//
        if (documentFilter.getAuto() != 0) {
            where += " and mixer_id = :auto";
        }

        if (documentFilter.isDebt()) {
            where += " and debt_total_value != 0 ";
        }

        if (where.substring(1,4).trim().equals("and")){
            where = where.substring(4, where.length());
        }

        String jSql = "select * from lb_purchase t where " + where + " order by t.id desc";

        Query selQuery = entityManager.createNativeQuery(jSql, LBPurchase.class);


        if (StringUtil.isNotEmpty(documentFilter.getClient())) {
            selQuery.setParameter("customer", documentFilter.getClient());
        }
        if (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null) {
            selQuery.setParameter("beginDate", documentFilter.getBeginDate());

            selQuery.setParameter("endDate", documentFilter.getEndDate());
        }else{

            selQuery.setParameter("beginDate",purchaseService.getBeginDate(LocalDate.now()));

            selQuery.setParameter("endDate", purchaseService.getEndDate(LocalDate.now()));
        }

        if (documentFilter.getMark() != 0) {
            selQuery.setParameter("mark", documentFilter.getMark());
        }

        if (documentFilter.getAuto() != 0) {
            selQuery.setParameter("mixer_id", documentFilter.getAuto());
        }

        if (documentFilter.getAmount() != 0) {
            selQuery.setParameter("amount", documentFilter.getAmount());
        }

        List<LBPurchase> purchases = selQuery.getResultList();
        return purchases;
    }
}
