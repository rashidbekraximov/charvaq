package uz.cluster.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "remainder")
public class Remainder extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "amount")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "mchj", columnDefinition = "varchar(5) default 'CHSM'")
    private MCHJ mchj;

    @Transient
    private int productTypeId;

    @Transient
    private int unitId;

    public RemainderDao asDao(){
        RemainderDao remainderDao = new RemainderDao();
        remainderDao.setId(getId());
        remainderDao.setProductType(getProductType());
        remainderDao.setProductTypeId(getProductTypeId());
        remainderDao.setUnit(getUnit());
        remainderDao.setUnitId(getUnitId());
        remainderDao.setAmount(getAmount());
        remainderDao.setMchj(getMchj());
        return remainderDao;
    }
}
