package uz.cluster.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.OrderProductDao;
import uz.cluster.dao.purchase.PurchasedProductDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordered_product")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name = "weight")
    private double weight;

    @Column(name = "price")
    private double price;

    @Column(name = "purchase_id")
    private int purchaseId;

    @Column(name = "value")
    private double value;

    @Transient
    private int productTypeId;

    public OrderProductDao asDao(){
        OrderProductDao price = new OrderProductDao();
        price.setId(getId());
        price.setProductType(getProductType());
        price.setProductTypeId(getProductTypeId());
        price.setPrice(getPrice());
        price.setWeight(getWeight());
        price.setValue(getValue());
        return price;
    }
}
