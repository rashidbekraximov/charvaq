package uz.cluster.entity.lb;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.lb.LBPriceDao;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.Status;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "lb_price")
public class LBPrice extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private uz.cluster.enums.Status status = Status.ACTIVE;

    @Transient
    private int productTypeId;

    public LBPriceDao asDao(){
        LBPriceDao price = new LBPriceDao();
        price.setId(getId());
        price.setProductType(getProductType());
        price.setProductTypeId(getProductTypeId());
        price.setPrice(getPrice());
        price.setStatus(getStatus());
        return price;
    }
}

