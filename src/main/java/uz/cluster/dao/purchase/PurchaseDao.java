package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.purchase.Order;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.entity.references.model.PaymentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDao extends BaseDao {

    private int key;

    private String checkNumber;

    private String documentCode;

    private String client;

    private String phoneNumber;

    private LocalDate date;

    private LocalDate expiryDate;

    private Technician technician;

    private PaymentType paymentType;

    private int technicianId;

    private int paymentTypeId;

    private Order order;

    private int orderId;

    private String location;

    private double km;

    private double fare;

    private double totalValue;

    private double paidTotalValue;

    private double debtTotalValue;

    private String description;

    List<PurchasedProductDao> purchasedProductList = new ArrayList<>();

    public Purchase copy(PurchaseDao purchaseDao){
        Purchase price = new Purchase();
        price.setId((int) purchaseDao.getId());
        price.setCheckNumber(purchaseDao.getCheckNumber());
        price.setClient(purchaseDao.getClient());
        price.setPhoneNumber(purchaseDao.getPhoneNumber());
        price.setDate(purchaseDao.getDate());
        price.setExpiryDate(purchaseDao.getExpiryDate());
        price.setTechnician(purchaseDao.getTechnician());
        price.setTechnicianId(purchaseDao.getTechnicianId());
        price.setPaymentType(getPaymentType());
        price.setPaymentTypeId(getPaymentTypeId());
        price.setOrder(purchaseDao.getOrder());
        price.setOrderId(purchaseDao.getOrderId());
        price.setLocation(purchaseDao.getLocation());
        price.setKm(purchaseDao.getKm());
        price.setFare(purchaseDao.getFare());
        price.setTotalValue(purchaseDao.getTotalValue());
        price.setPaidTotalValue(purchaseDao.getPaidTotalValue());
        price.setDebtTotalValue(purchaseDao.getDebtTotalValue());
        price.setDescription(purchaseDao.getDescription());
        for (PurchasedProductDao purchasedProductDao : purchaseDao.getPurchasedProductList()){
            price.getPurchasedProductList().add(purchasedProductDao.copy(purchasedProductDao));
        }
        return price;
    }
}
