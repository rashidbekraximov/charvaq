package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.*;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.entity.lb.*;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.*;
import uz.cluster.repository.purchase.RemainderRepository;
import uz.cluster.services.purchase.PurchaseService;
import uz.cluster.util.LanguageManager;
import uz.cluster.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class LBPurchaseService {

    private final EntityManager entityManager;

    private final LBPurchaseRepository lbPurchaseRepository;

    private final MixerRepository mixerRepository;

    private final RemainderRepository remainderRepository;

    private final LBCostRepository lbCostRepository;

    private final LBPriceService lbPriceService;

    private final PurchaseService purchaseService;

    private final IngredientService ingredientService;

    private final MixerUseService mixerUseService;

    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_VIEW)
    public List<LBPurchaseDao> getList() {
        return lbPurchaseRepository.findAll().stream().map(LBPurchase::asDao).collect(Collectors.toList());
    }

    public List<LBPurchaseDao> getLastRows() {
        return lbPurchaseRepository.getAllByDesc().stream().map(LBPurchase::asDao).collect(Collectors.toList());
    }


    public List<LBMainDao> getDashboardData(String  mchj,String beginDate,String endDate){
        List<LBMainDao> daoList = new ArrayList<>();
        List<LBSaleData> saleData = lbCostRepository.geCountOfNoDebtForBarChart(beginDate,endDate,mchj);
        LBMainDao lbMainDao1 = new LBMainDao();
        lbMainDao1.setName("Auto xarajati");
        LBMainDao lbMainDao2 = new LBMainDao();
        lbMainDao2.setName("Ishlab chiqarish xarajati");
        saleData.forEach(s -> {
            lbMainDao1.setAmount(s.getAutoCostAmount());
            lbMainDao2.setAmount(s.getProductCostAmount());
            lbMainDao1.setNum(s.getNum());
            lbMainDao2.setNum(s.getNum());
        });

        daoList.add(lbMainDao1);
        daoList.add(lbMainDao2);

        LBMainDao lbMainDao3 = new LBMainDao();
        lbMainDao3.setName("Umumiy xarajat");
        lbMainDao3.setNum(lbMainDao1.getNum());
        lbMainDao3.setAmount(lbMainDao1.getAmount() + lbMainDao2.getAmount());
        daoList.add(lbMainDao3);

        List<BarChart> incomes = lbPurchaseRepository.getAllIncomes(beginDate,endDate,mchj);
        LBMainDao lbMainDao4 = new LBMainDao();
        lbMainDao4.setName("Daromad");
        incomes.forEach(i -> {
            lbMainDao4.setNum(i.getNum());
            lbMainDao4.setAmount(i.getAmount());
        });
        daoList.add(lbMainDao4);

        LBMainDao lbMainDao5 = new LBMainDao();
        lbMainDao5.setName("Sof foyda");
        lbMainDao5.setNum(lbMainDao1.getNum());
        lbMainDao5.setAmount(lbMainDao4.getAmount() - lbMainDao3.getAmount());
        daoList.add(lbMainDao5);

        return IntStream.range(0, daoList.size()).map(i -> daoList.size() - 1-i).mapToObj(daoList::get).collect(Collectors.toList());
    }


    public List<Long> getBarList() {
        List<Long> lineChartDaoList = new ArrayList<>();
        for (int j = 1; j < 13; j++) {
            long numsMonthLy = lbPurchaseRepository.getAllPurchaseNumberForBarChart(j);
            lineChartDaoList.add(numsMonthLy);
        }
        return lineChartDaoList;
    }

    public List<Double> getBarListDaily(String beginDate,String endDate) {
        List<Double> lineChartDaoList = new ArrayList<>();
        List<LBPurchase> purchaseList = lbPurchaseRepository.getAllPurchaseNumberForBarChartDaily(beginDate, endDate);
        double income = 0;
        double hour = 0;
        for(LBPurchase purchase : purchaseList){
            income += purchase.getNasos();
            hour += purchase.getHour();
        }
        lineChartDaoList.add(income);
        lineChartDaoList.add(hour);
        return lineChartDaoList;
    }

    public List<Long> getBarListForNasos() {
        List<Long> lineChartDaoList = new ArrayList<>();
        for (int j = 1; j < 13; j++) {
            long numsMonthLy = lbPurchaseRepository.getAllHireForNasos(j);
            lineChartDaoList.add(numsMonthLy);
        }
        return lineChartDaoList;
    }

    public List<BarDao> getBarLast(String company,String beginDate,String endDate) {
        List<BarDao> lineChartDaoList = new ArrayList<>();
        List<BarChart> bars =  lbPurchaseRepository.getAllByMarkDaily(beginDate, endDate,company);
        bars.forEach(bar -> {
            BarDao barDao = new BarDao();
            barDao.setMark(bar.getMark());
            barDao.setAmount(bar.getAmount());
            barDao.setNum(bar.getNum());
            lineChartDaoList.add(barDao);
        });
        return lineChartDaoList;
    }

    public LBPurchaseDao getById(int id) {
        Optional<LBPurchase> optional = lbPurchaseRepository.findById((long) id);
        if (optional.isEmpty()) {
            return null;
        } else {
            return optional.get().asDao();
        }
    }

    public ApiResponse getByAmountAndMark(int mark, double amount,MCHJ mchj) {
        IngredientDao lbPurchase = ingredientService.getByAmountAndMark(mark, amount);
        Remainder remainder1 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SEMENT.getValue(), mchj).orElse(new Remainder());
        boolean test = false;
        if (!(remainder1.getAmount() - lbPurchase.getSement() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        Remainder remainder2 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.KLINES.getValue(), mchj).orElse(new Remainder());
        if (!(remainder2.getAmount() - lbPurchase.getKlines() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        Remainder remainder3 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SHEBEN.getValue(), mchj).orElse(new Remainder());
        if (!(remainder3.getAmount() - lbPurchase.getSheben() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        Remainder remainder4 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.CHINOZ.getValue(), mchj).orElse(new Remainder());
        if (!(remainder4.getAmount() - lbPurchase.getPesok() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        Remainder remainder5 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.DOBAVKA.getValue(), mchj).orElse(new Remainder());
        if (!(remainder5.getAmount() - lbPurchase.getDobavka() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        Remainder remainder6 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.ANTIMAROZ.getValue(), mchj).orElse(new Remainder());
        if (!(remainder6.getAmount() - lbPurchase.getAntimaroz() >= 0)){
            return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
        }

        return new ApiResponse(true, LanguageManager.getLangMessage("enough"));
    }


    @CheckPermission(form = FormEnum.LB_PURCHASE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(LBPurchaseDao lbPurchaseDao) {
        LBPurchase lbPurchase = lbPurchaseDao.copy(lbPurchaseDao);

        ApiResponse apiResponse = getByAmountAndMark(lbPurchase.getMark(),lbPurchase.getAmount(),lbPurchase.getMchj());
        if (!apiResponse.isSuccess()){
            return apiResponse;
        }

        double autoCostAmount = 0;
        for (MixerUse mixerUse : lbPurchase.getMixerUses()){
            if (mixerUse.getMixerId() == 0){
                return new ApiResponse(true, LanguageManager.getLangMessage("no_data_submitted"));
            }
            Optional<Mixer> optional =  mixerRepository.findById(mixerUse.getMixerId());
            if(optional.isPresent()){
                mixerUse.setMixer(optional.get());
                autoCostAmount += optional.get().getPerKmCostAmount() * lbPurchase.getKm();
            }
        }


        double costAmount = 0;
        Optional<Remainder> remainder1 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SEMENT.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice1 = lbPriceService.getPriceById(PurchaseEnum.SEMENT.getValue());
        if (remainder1.isPresent() && remainder1.get().getAmount() - lbPurchase.getSement() >= 0){
            remainder1.get().setAmount(remainder1.get().getAmount() - lbPurchase.getSement());
            costAmount += lbPurchase.getSement() * lbPrice1.getPrice();
            remainderRepository.save(remainder1.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        Optional<Remainder> remainder2 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.KLINES.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice2 = lbPriceService.getPriceById(PurchaseEnum.KLINES.getValue());
        if (remainder2.isPresent() && remainder2.get().getAmount() - lbPurchase.getKlines() >= 0){
            remainder2.get().setAmount(remainder2.get().getAmount() - lbPurchase.getKlines());
            costAmount += lbPurchase.getKlines() * lbPrice2.getPrice();
            remainderRepository.save(remainder2.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        Optional<Remainder> remainder3 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SHEBEN.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice3 = lbPriceService.getPriceById(PurchaseEnum.SHEBEN.getValue());
        if (remainder3.isPresent() && remainder3.get().getAmount() - lbPurchase.getSheben() >= 0){
            remainder3.get().setAmount(remainder3.get().getAmount() - lbPurchase.getSheben());
            costAmount += lbPurchase.getSheben() * lbPrice3.getPrice();
            remainderRepository.save(remainder3.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        Optional<Remainder> remainder4 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.CHINOZ.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice4 = lbPriceService.getPriceById(PurchaseEnum.CHINOZ.getValue());
        if (remainder4.isPresent() && remainder4.get().getAmount() - lbPurchase.getPesok() >= 0){
            remainder4.get().setAmount(remainder4.get().getAmount() - lbPurchase.getPesok());
            costAmount += lbPurchase.getPesok() * lbPrice4.getPrice();
            remainderRepository.save(remainder4.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        Optional<Remainder> remainder5 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.DOBAVKA.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice5 = lbPriceService.getPriceById(PurchaseEnum.DOBAVKA.getValue());
        if (remainder5.isPresent() && remainder5.get().getAmount() - lbPurchase.getDobavka() >= 0){
            remainder5.get().setAmount(remainder5.get().getAmount() - lbPurchase.getDobavka());
            costAmount += lbPurchase.getDobavka() * lbPrice5.getPrice();
            remainderRepository.save(remainder5.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        Optional<Remainder> remainder6 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.ANTIMAROZ.getValue(), lbPurchase.getMchj());
        LBPrice lbPrice6 = lbPriceService.getPriceById(PurchaseEnum.ANTIMAROZ.getValue());
        if (remainder6.isPresent() && remainder6.get().getAmount() - lbPurchase.getAntimaroz() >= 0){
            remainder6.get().setAmount(remainder6.get().getAmount() - lbPurchase.getAntimaroz());
            costAmount += lbPurchase.getAntimaroz() * lbPrice6.getPrice();
            remainderRepository.save(remainder6.get());
        }else{
            return new ApiResponse(false, "Narx yoki omborda mahsulot yetarli emas :(");
        }

        LBPurchase saved = null;
        try {
            saved = lbPurchaseRepository.save(lbPurchase);
            saveCost(saved,lbPurchase.getMchj(),costAmount,autoCostAmount);
            log.info("LB Sotuv saqlandi !");
        }catch (Exception e){
            log.error("LB Sotuvni saqlashda xatolik !");
        }
        for (MixerUse mixerUse : lbPurchase.getMixerUses()) {
            mixerUse.setLbPurchase(saved);
            mixerUseService.saveMixerUse(mixerUse);
        }

        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    public void saveCost(LBPurchase saved,MCHJ mchj,double amount,double autoCostAmount){
        LBCost lbCost = new LBCost();
        lbCost.setMchj(mchj);
        lbCost.setPurchaseId(saved.getId());
        lbCost.setDate(saved.getDate());
        lbCost.setProductCostAmount(amount);
        lbCost.setAutoCostAmount(autoCostAmount);
        LBCost lbCost1 = lbCostRepository.save(lbCost);
        System.out.println(lbCost1);
    }

    public ApiResponse restore(LBPurchase lbPurchase){
        Optional<Remainder> remainder1 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SEMENT.getValue(), lbPurchase.getMchj());
        if (remainder1.isPresent()){
            remainder1.get().setAmount(remainder1.get().getAmount() - lbPurchase.getSement());
            remainderRepository.save(remainder1.get());
        }else{
            return new ApiResponse(false, "Sementni omborga qaytarishda xatolik yuzaga keldi");
       }

        Optional<Remainder> remainder2 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.KLINES.getValue(), lbPurchase.getMchj());
        if (remainder2.isPresent()){
            remainder2.get().setAmount(remainder2.get().getAmount() + lbPurchase.getKlines());
            remainderRepository.save(remainder2.get());
        }else{
            return new ApiResponse(false, "KLINESni omborga qaytarishda xatolik yuzaga keldi");
        }

        Optional<Remainder> remainder3 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.SHEBEN.getValue(), lbPurchase.getMchj());
        if (remainder3.isPresent()){
            remainder3.get().setAmount(remainder3.get().getAmount() + lbPurchase.getSheben());
            remainderRepository.save(remainder3.get());
        }else{
            return new ApiResponse(false, "SHEBENni omborga qaytarishda xatolik yuzaga keldi");
       }

        Optional<Remainder> remainder4 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.CHINOZ.getValue(), lbPurchase.getMchj());
        if (remainder4.isPresent()){
            remainder4.get().setAmount(remainder4.get().getAmount() + lbPurchase.getPesok());
            remainderRepository.save(remainder4.get());
        }else{
            return new ApiResponse(false, "CHINOZni omborga qaytarishda xatolik yuzaga keldi");
       }

        Optional<Remainder> remainder5 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.DOBAVKA.getValue(), lbPurchase.getMchj());
        if (remainder5.isPresent()){
            remainder5.get().setAmount(remainder5.get().getAmount() + lbPurchase.getDobavka());
            remainderRepository.save(remainder5.get());
        }else{
            return new ApiResponse(false, "DOBAVKAni omborga qaytarishda xatolik yuzaga keldi");
      }

        Optional<Remainder> remainder6 = remainderRepository.findByProductType_IdAndMchj(PurchaseEnum.ANTIMAROZ.getValue(), lbPurchase.getMchj());
        if (remainder6.isPresent()){
            remainder6.get().setAmount(remainder6.get().getAmount() + lbPurchase.getAntimaroz());
            remainderRepository.save(remainder6.get());
        }else{
            return new ApiResponse(false, "ANTIMAROZni omborga qaytarishda xatolik yuzaga keldi");
       }
        return new ApiResponse(true, "Mahsulotni omborga qaytarishda xatoolik yuzaga keldi");
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
            ApiResponse api = restore(optional.get());
            if (!api.isSuccess()){
                return api;
            }
            try{
                lbCostRepository.deleteAllByPurchaseId(id);
                log.info("Hamma xarajatlar o'chirildi !");
                lbPurchaseRepository.deleteById(id);
                log.info("LB Sotuv o'chirildi !");
            }catch (Exception e){
                log.error("Hamma xarajatlar o'chirishda xatolik yuzaga keldi !");
            }
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
        if (documentFilter.getMchj() != null) {
            where += " and mchj = :mchj";
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

        if (documentFilter.getMchj() != null) {
            selQuery.setParameter("mchj", documentFilter.getMchj().name());
        }

        if (documentFilter.getAmount() != 0) {
            selQuery.setParameter("amount", documentFilter.getAmount());
        }

        List<LBPurchase> purchases = selQuery.getResultList();
        return purchases;
    }
}
