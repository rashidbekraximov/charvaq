package uz.cluster.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.PurchasedProductDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.SexEnum;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "purchased_product")
public class PurchasedProduct extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "technician_id")
    private int technicianId;

    @Column(name = "purchase_id")
    private int purchaseId;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name = "weight")
    private double weight;

    @Column(name = "price")
    private double price;

    @Column(name = "value")
    private double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private SexEnum sexEnum;

    @Transient
    private int productTypeId;

    public PurchasedProductDao asDao(){
        PurchasedProductDao price = new PurchasedProductDao();
        price.setId(getId());
        price.setProductType(getProductType());
        price.setProductTypeId(getProductTypeId());
        price.setPrice(getPrice());
        price.setWeight(getWeight());
        price.setValue(getValue());
        price.setSexEnum(getSexEnum());
        return price;
    }
}