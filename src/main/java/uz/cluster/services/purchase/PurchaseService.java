package uz.cluster.services.purchase;


import lombok.RequiredArgsConstructor;
import org.hibernate.SynchronizeableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.*;
import uz.cluster.entity.Document;
import uz.cluster.entity.auth.User;
import uz.cluster.entity.logistic.BringDrobilkaProduct;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.entity.purchase.*;
import uz.cluster.entity.references.model.*;
import uz.cluster.enums.PaymentEnum;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.TechnicianRepository;
import uz.cluster.repository.produce.ReadyProductRepository;
import uz.cluster.repository.purchase.*;
import uz.cluster.repository.references.PaymentTypeRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.services.logistic.LogisticService;
import uz.cluster.services.produce.ReadyProductService;
import uz.cluster.util.LanguageManager;
import uz.cluster.util.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final EntityManager entityManager;

    private final PurchasedProductRepository purchasedProductRepository;

    private final RemainderRepository remainderRepository;

    private final TechnicianRepository technicianRepository;

    private final PurchaseRepository purchaseRepository;

    private final ProductTypeRepository productTypeRepository;

    private final PaymentTypeRepository paymentTypeRepository;

    private final OrderRepository orderRepository;

    private final RemainderService remainderService;

    private final LogisticService logisticService;

    private final DailyIncomeService dailyIncomeService;

    private final ReadyProductRepository readyProductRepository;

    private final DailyIncomeRepository dailyIncomeRepository;


    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_VIEW)
    public List<PurchaseDao> getList() {
        return purchaseRepository.findAll().stream().map(Purchase::asDao).collect(Collectors.toList());
    }

    public List<PurchaseDao> getLastARow() {
        return purchaseRepository.getAllByDescForTallon().stream().map(Purchase::asDao).collect(Collectors.toList());
    }

    public List<PurchaseDao> getLastRows() {
        return purchaseRepository.getAllByDesc().stream().map(Purchase::asDao).collect(Collectors.toList());
    }

    //Kassani tekshirish uchun
    public List<PurchaseDao> getByDateForConfirm(Date date) {
        return purchaseRepository.getAllByDate(date).stream().map(Purchase::asDao).collect(Collectors.toList());
    }

    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_VIEW)
    public List<PurchaseDao> getSearchList(String client) {
        if (!client.isEmpty() && !client.trim().isEmpty()) {
            return purchaseRepository.getSearchedDebts(client.toLowerCase()).stream().map(Purchase::asDao).collect(Collectors.toList());
        } else {
            return purchaseRepository.getAllDebts().stream().map(Purchase::asDao).collect(Collectors.toList());
        }
    }

    public PurchaseDao getById(int id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) {
            return null;
        } else {
            optionalPurchase.get().setPurchasedProductList(purchasedProductRepository.findAllByPurchaseIdOrderByProductType(id));
            return optionalPurchase.get().asDao();
        }
    }

    public List<LineChartDao> getLineList() {
        List<LineChartDao> lineChartDaoList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            LineChartDao line = new LineChartDao();
            line.setId((byte) i);
            List<Double> amount = new ArrayList<>();
            for (int j = 1; j < 13; j++) {
                double a = 0;
                List<Integer> monthValueList = purchaseRepository.getAllPurchaseForLineChart(j);
                for (Integer id : monthValueList) {
                    List<Double> optional = purchasedProductRepository.getSumValueForLineChart(i, id);
                    for (Double d : optional) {
                        a += d;
                    }
                }
                amount.add(a);
            }
            line.setMonthValue(amount);
            lineChartDaoList.add(line);
        }

        return lineChartDaoList;
    }

    public List<Long> getBarList() {
        List<Long> lineChartDaoList = new ArrayList<>();
        for (int j = 1; j < 13; j++) {
            long numsMonthLy = purchaseRepository.getAllPurchaseNumberForBarChart(j);
            lineChartDaoList.add(numsMonthLy);
        }
        return lineChartDaoList;
    }

    public List<PurchaseData> getBarLastMonthly(String beginDate,String endDate) {
        List<PurchaseData> lineChartDaoList = new ArrayList<>();
        List<SaleData> numPurchase = purchaseRepository.geCountOfPurchaseForBarChartMonthly(beginDate, endDate);
        numPurchase.forEach(val -> {
            lineChartDaoList.add(new PurchaseData(val.getNum(),val.getAmount()));
        });
        List<SaleData> numPurchaseDebt = purchaseRepository.geCountOfDebtForBarChartMonthly(beginDate, endDate);
        numPurchaseDebt.forEach(val -> {
            lineChartDaoList.add(new PurchaseData(val.getNum(),val.getAmount()));
        });
        List<SaleData> numPurchaseNoDebt = purchaseRepository.geCountOfNoDebtForBarChartMonthly(beginDate, endDate);
        numPurchaseNoDebt.forEach(val -> {
            lineChartDaoList.add(new PurchaseData(val.getNum(),val.getAmount()));
        });
        return lineChartDaoList;
    }

    public List<Notification> getNotifications(int rows){
        List<Notification> notifications = new ArrayList<>();
        List<NotificationDto> notificationDtos = purchaseRepository.getAllDebt(rows);
        notificationDtos.forEach(dto -> {
            Notification notification = new Notification();
            notification.setId(dto.getId());
            notification.setDays(dto.getDays());
            notification.setClient(dto.getClient());
            notification.setLocation(dto.getLocation());
            notification.setDebtTotalValue(dto.getDebtTotalValue());
            notifications.add(notification);
        });
        return notifications;
    }


    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(PurchaseDao purchaseDao) {
        Purchase purchase = purchaseDao.copy(purchaseDao);

        if (purchase.getPaymentTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        Optional<Technician> optionalTechnician = technicianRepository.findById(purchase.getTechnicianId());
        optionalTechnician.ifPresent(purchase::setTechnician);

        Optional<PaymentType> optionalPaymentType = paymentTypeRepository.findById(purchase.getPaymentTypeId());
        optionalPaymentType.ifPresent(purchase::setPaymentType);

        for (PurchasedProduct purchasedProduct : purchase.getPurchasedProductList()) {
            if (purchasedProduct.getProductTypeId() == 0) {
                return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
            }
            Optional<ProductType> optionalProductType = productTypeRepository.findById(purchasedProduct.getProductTypeId());
            optionalProductType.ifPresent(purchasedProduct::setProductType);
            purchasedProduct.setTechnicianId(purchase.getTechnicianId());
            if (purchase.getId() == 0) {
                if (remainderService.purchase(purchasedProduct.getProductTypeId(),purchasedProduct.getWeight(),purchasedProduct.getSexEnum())){
                    return new ApiResponse(false, LanguageManager.getLangMessage("not_found_all"));
                }
            }
        }
        if (purchase.getOrderId() != 0) {
            Optional<Order> orderOptional = orderRepository.findById(purchase.getOrderId());
            if (orderOptional.isPresent()) {
                orderOptional.get().setStatus(Status.PASSIVE);
                purchase.setOrder(orderOptional.get());
            }
        }

        if (purchase.getId() != 0) {
            return edit(purchase);
        }
        if (PaymentEnum.NAQD.getValue() == purchase.getPaymentTypeId()){
            dailyIncomeService.addFromPurchase(purchase);
        }

        Purchase saved = purchaseRepository.save(purchase);
        for (PurchasedProduct purchasedProduct : purchase.getPurchasedProductList()) {
            purchasedProduct.setPurchaseId(saved.getId());
        }
        purchasedProductRepository.saveAll(purchase.getPurchasedProductList());
        if (optionalTechnician.isPresent()) {
            BringDrobilkaProduct bringDrobilkaProduct = new BringDrobilkaProduct();
            bringDrobilkaProduct.setId(purchase.getId());
            bringDrobilkaProduct.setDate(purchase.getDate());
            bringDrobilkaProduct.setTechnician(purchase.getTechnician());
            bringDrobilkaProduct.setKm(purchase.getKm());
            logisticService.createBringProductLogisticCost(bringDrobilkaProduct,true);
        }
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Purchase purchase) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchase.getId());
        if (optionalPurchase.isPresent()) {
            List<PurchasedProduct> products = purchasedProductRepository.findAllByPurchaseIdOrderByProductType(optionalPurchase.get().getId());
            if (optionalPurchase.get().getOrderId() != 0) {
                Optional<Order> orderOptional = orderRepository.findById(purchase.getOrderId());
                orderOptional.ifPresent(order -> order.setStatus(Status.ACTIVE));
            }
            for (PurchasedProduct product : purchase.getPurchasedProductList()) {
                product.setPurchaseId(purchase.getId());
                for (PurchasedProduct purchasedProduct : products) {
                    if (product.getProductType().getId() == purchasedProduct.getProductType().getId()) {
                        Optional<ReadyProduct> optionalReadyProduct = readyProductRepository.findByProductType_Id(product.getProductType().getId());
                        if (optionalReadyProduct.isPresent()) {
                            optionalReadyProduct.get().setAmount((optionalReadyProduct.get().getAmount() + purchasedProduct.getWeight()) - product.getWeight());
                            readyProductRepository.save(optionalReadyProduct.get());
                        }
                    }
                }
            }
            Purchase edited = purchaseRepository.save(purchase);
            purchasedProductRepository.deleteAllByPurchaseId(edited.getId());
            logisticService.deleteAllById(edited.getId());
            purchasedProductRepository.saveAll(purchase.getPurchasedProductList());
            if (edited.getTechnician() != null) {
                BringDrobilkaProduct bringDrobilkaProduct = new BringDrobilkaProduct();
                bringDrobilkaProduct.setId(edited.getId());
                bringDrobilkaProduct.setDate(edited.getDate());
                bringDrobilkaProduct.setTechnician(edited.getTechnician());
                bringDrobilkaProduct.setKm(edited.getKm());
                logisticService.createBringProductLogisticCost(bringDrobilkaProduct,true);
            }
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        } else {
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isPresent()) {
            Optional<DailyIncome> optional = dailyIncomeRepository.findByDate(optionalPurchase.get().getDate());
            optional.ifPresent(dailyIncome -> dailyIncome.setIncome(dailyIncome.getIncome() - optionalPurchase.get().getPaidTotalValue()));
            purchaseRepository.deleteById(id);
            logisticService.deleteAllById(id);
            for (PurchasedProduct purchasedProduct : purchasedProductRepository.findAllByPurchaseIdOrderByProductType(id)){
                if (purchasedProduct.getProductType().getId() != PurchaseEnum.SH_BLOK.getValue()){
                    Remainder remainder = remainderService.getByProductId(purchasedProduct.getProductType().getId());
                    remainder.setAmount(remainder.getAmount() + purchasedProduct.getWeight());
                    remainderRepository.save(remainder);
                }else{
                    Remainder remainder = remainderService.getByProductIdAndSexEnum(purchasedProduct.getProductType().getId(),purchasedProduct.getSexEnum());
                    remainder.setAmount(remainder.getAmount() + purchasedProduct.getWeight());
                    remainderRepository.save(remainder);
                }
            }
            purchasedProductRepository.deleteAllByPurchaseId(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        } else {
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse deleteDebt(int id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isPresent()) {
            optionalPurchase.get().setStatus(Status.PASSIVE);
            optionalPurchase.get().setDebtTotalValue(0);
            optionalPurchase.get().setPaidTotalValue(optionalPurchase.get().getTotalValue());
            Purchase passive = purchaseRepository.save(optionalPurchase.get());
            purchasedProductRepository.deleteAllByPurchaseId(id);
            logisticService.deleteAllById(id);
            return new ApiResponse(true, passive, LanguageManager.getLangMessage("deleted"));
        } else {
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.PURCHASE, permission = Action.CAN_VIEW)
    @Transactional
    public List<Purchase> getPurchasesByPage(DocumentFilter documentFilter) {
        if (documentFilter == null) {
            documentFilter = new DocumentFilter();
        }

        String where = "";

        if (StringUtil.isNotEmpty(documentFilter.getClient())) {
            where += " and client like  '%'||:client||'%' ";
        }

        if (StringUtil.isNotEmpty(documentFilter.getCheckNumber())) {
            where += " and check_number like  '%'||:check_number||'%' ";
        }

        if (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null) {
            where += " and t.date between :beginDate and :endDate";
        } else {
            if (documentFilter.getId() != 0 || StringUtil.isNotEmpty(documentFilter.getCheckNumber()) || StringUtil.isNotEmpty(documentFilter.getClient()) || (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null)) {
                where += " and t.date between :beginDate and :endDate ";
            } else {
                where += "t.date between :beginDate and :endDate ";
            }
        }

        if (documentFilter.getPaymentTypeId() != 0) {
            where += " and payment_type_id = :paymentTypeId";
        }

        if (documentFilter.getTechnicianId() != 0) {
            where += " and technician_id = :technicianId";
        }

        if (documentFilter.isDebt()) {
            where += " and debt_total_value != 0 ";
        }

        if (where.substring(1, 4).trim().equals("and")) {
            where = where.substring(4, where.length());
        }

        String jSql = "select t.* from purchase t where " + where + " order by t.id desc";
//        String jSql = "select t.* from purchase t ";

        Query selQuery = this.entityManager.createNativeQuery(jSql, Purchase.class);


        if (StringUtil.isNotEmpty(documentFilter.getCheckNumber())) {
            selQuery.setParameter("check_number", documentFilter.getCheckNumber());
        }
        if (StringUtil.isNotEmpty(documentFilter.getClient())) {
            selQuery.setParameter("client", documentFilter.getClient());
        }
        if (documentFilter.getBeginDate() != null && documentFilter.getEndDate() != null) {
            selQuery.setParameter("beginDate", documentFilter.getBeginDate());

            selQuery.setParameter("endDate", documentFilter.getEndDate());
        } else {
            selQuery.setParameter("beginDate", LocalDate.now());

            selQuery.setParameter("endDate", LocalDate.now());
        }

        if (documentFilter.getPaymentTypeId() != 0) {
            selQuery.setParameter("paymentTypeId", documentFilter.getPaymentTypeId());
        }

        if (documentFilter.getTechnicianId() != 0) {
            selQuery.setParameter("technicianId", documentFilter.getTechnicianId());
        }

        @SuppressWarnings("unchecked")
        List<Purchase> purchases = selQuery.getResultList();
        return purchases;
    }

    public LocalDate getBeginDate(LocalDate date) {
        return LocalDate.of(date.getYear(), (date.getMonth()), 1);
    }

    public LocalDate getEndDate(LocalDate date) {

        return LocalDate.of(date.getYear(), (date.getMonth()), date.getDayOfMonth());
    }
}
