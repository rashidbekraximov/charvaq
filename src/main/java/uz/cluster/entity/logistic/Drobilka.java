package uz.cluster.entity.logistic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.logistic.DrobilkaDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.DrobilkaType;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "drobilka")
public class Drobilka extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "drobilka_type_id")
    private DrobilkaType drobilkaType;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "amount")
    private double amount;

    @Transient
    private int productTypeId;

    @Transient
    private int drobilkaTypeId;

    @Transient
    private int unitId;

    public DrobilkaDao asDao(){
        DrobilkaDao drobilkaDao = new DrobilkaDao();
        drobilkaDao.setId(getId());
        drobilkaDao.setProductType(getProductType());
        drobilkaDao.setProductTypeId(getProductTypeId());
        drobilkaDao.setDrobilkaType(getDrobilkaType());
        drobilkaDao.setDrobilkaTypeId(getDrobilkaTypeId());
        drobilkaDao.setUnit(getUnit());
        drobilkaDao.setUnitId(getUnitId());
        drobilkaDao.setAmount(getAmount());
        return drobilkaDao;
    }
}
