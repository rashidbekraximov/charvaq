package uz.cluster.entity.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.OrderDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.Status;

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
@Table(name = "order")
public class Order extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "name")
    private String client;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate date;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "location")
    private String location;

    @Column(name = "fare")
    private double fare;

    @Column(name = "km")
    private double km;

    @Column(name = "value")
    private double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Transient
    private int productTypeId;

    @Transient
    List<OrderedProduct> purchasedProductList = new ArrayList<>();

    public OrderDao asDao(){
        OrderDao order = new OrderDao();
        order.setId(getId());
        order.setClient(getClient());
        order.setPhoneNumber(getPhoneNumber());
        order.setLocation(getLocation());
        order.setDate(getDate());
        order.setKm(getKm());
        order.setTotalValue(getValue());
        order.setFare(getFare());
        order.setStatus(getStatus());
        for (OrderedProduct orderedProduct : getPurchasedProductList()){
            order.getPurchasedProductList().add(orderedProduct.asDao());
        }
        return order;
    }
}

