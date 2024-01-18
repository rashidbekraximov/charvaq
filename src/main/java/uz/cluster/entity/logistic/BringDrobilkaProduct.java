package uz.cluster.entity.logistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.logistic.BringDrobilkaProductDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.DrobilkaType;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "bring_drobilka_product")
public class BringDrobilkaProduct extends Auditable {

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
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "drobilka_amount",columnDefinition = "real default 0")
    private double drobilkaAmount;

    @Column(name = "difference",columnDefinition = "real default 0")
    private double difference;

    @Column(name = "amount")
    private double amount;

    @Column(name = "income")
    private double income;

    @Column(name = "km")
    private double km;

    @Enumerated(EnumType.STRING)
    @Column(name = "mchj", columnDefinition = "varchar(5) default 'CHSM'")
    private MCHJ mchj;

    @Transient
    private int productTypeId;

    @Transient
    private int drobilkaTypeId;

    @Transient
    private int unitId;

    @Transient
    private int technicianId;

    public BringDrobilkaProductDao asDao(){
        BringDrobilkaProductDao bringDrobilkaProductDao = new BringDrobilkaProductDao();
        bringDrobilkaProductDao.setId(getId());
        bringDrobilkaProductDao.setProductType(getProductType());
        bringDrobilkaProductDao.setProductTypeId(getProductTypeId());
        bringDrobilkaProductDao.setDrobilkaType(getDrobilkaType());
        bringDrobilkaProductDao.setDrobilkaTypeId(getDrobilkaTypeId());
        bringDrobilkaProductDao.setUnit(getUnit());
        bringDrobilkaProductDao.setUnitId(getUnitId());
        bringDrobilkaProductDao.setTechnician(getTechnician());
        bringDrobilkaProductDao.setTechnicianId(getTechnicianId());
        bringDrobilkaProductDao.setAmount(getAmount());
        bringDrobilkaProductDao.setDrobilkaAmount(getDrobilkaAmount());
        bringDrobilkaProductDao.setKm(getKm());
        bringDrobilkaProductDao.setIncome(getIncome());
        bringDrobilkaProductDao.setDate(getDate());
        bringDrobilkaProductDao.setMchj(getMchj());
        return bringDrobilkaProductDao;
    }
}
