package uz.cluster.entity.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.PaymentType;
import uz.cluster.enums.Status;
import uz.cluster.util.DateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "purchase")
public class Purchase extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "check_number")
    private String checkNumber;

    @Column(name = "document_code",columnDefinition = "VARCHAR(15) default '' ")
    private String documentCode;

    @Column(name = "client")
    private String client;

    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "paid_date")
    private LocalDate paidDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "hired_car")
    private String hiredCar;

    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "location")
    private String location;

    @Column(name = "km")
    private double km;

    @Column(name = "fare")
    private double fare;

    @Column(name = "nasos")
    private double nasos;

    @Column(name = "total_value")
    private double totalValue;

    @Column(name = "paid_total_value")
    private double paidTotalValue;

    @Column(name = "debt_total_value")
    private double debtTotalValue;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Transient
    List<PurchasedProduct> purchasedProductList = new ArrayList<>();

    @Transient
    private int technicianId;

    @Transient
    private int paymentTypeId;

    @Transient
    private int orderId;

    public PurchaseDao asDao(){
        PurchaseDao price = new PurchaseDao();
        price.setId(getId());
        price.setKey(getId());
        price.setCheckNumber(getCheckNumber());
        price.setDocumentCode(getDocumentCode());
        price.setClient(getClient());
        price.setPhoneNumber(getPhoneNumber());
        price.setDate(getDate());
        price.setHiredCar(getHiredCar());
        price.setExpiryDate(getExpiryDate());
        price.setCreatedOn(getCreatedOnString());
        price.setTechnician(getTechnician());
        price.setTechnicianId(getTechnicianId());
        price.setPaymentType(getPaymentType());
        price.setPaymentTypeId(getPaymentTypeId());
        price.setOrder(getOrder());
        price.setLocation(getLocation());
        price.setKm(getKm());
        price.setFare(getFare());
        price.setTotalValue(getTotalValue());
        price.setPaidTotalValue(getPaidTotalValue());
        price.setDebtTotalValue(getDebtTotalValue());
        price.setDescription(getDescription());
        for (PurchasedProduct purchasedProduct : getPurchasedProductList()){
            price.getPurchasedProductList().add(purchasedProduct.asDao());
        }
        return price;
    }

    public String getCreatedOnString() {
        return DateUtil.convertToDateTimeString(getCreatedOn());
    }

}
