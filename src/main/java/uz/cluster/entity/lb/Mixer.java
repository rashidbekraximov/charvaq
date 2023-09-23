package uz.cluster.entity.lb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.lb.MixerDao;
import uz.cluster.entity.Auditable;
import uz.cluster.enums.Status;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "mixer")
public class Mixer extends Auditable {

    @Id
    @GeneratedValue(generator = "mixer_sq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "mixer_sq", sequenceName = "mixer_sq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "auto")
    private String auto;

    @Column(name = "gas_price")
    private double gasPrice;

    @Column(name = "per_km_gas_cost")
    private double perKmGasCost;

    @Column(name = "ballon")
    private int ballon;

    @Column(name = "ballon_amount")
    private double ballonAmount;

    @Column(name = "per_km_ballon_cost")
    private double perKmBallonCost;

    @Column(name = "oil_price")
    private double oilPrice;

    @Column(name = "per_km_oil_cost")
    private double perKmOilCost;

    @Column(name = "amortization")
    private double amortization;

    @Column(name = "per_km_amortization_cost")
    private double perKmAmortization;

    @Column(name = "auto_number")
    private String autoNumber;

    @Column(name = "per_km_cost_amount")
    private double perKmCostAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    public MixerDao asDao(){
        MixerDao technicianDao = new MixerDao();
        technicianDao.setId(getId());
        technicianDao.setGasPrice(getGasPrice());
        technicianDao.setPerKmGasCost(getPerKmGasCost());
        technicianDao.setBallon(getBallon());
        technicianDao.setBallonAmount(getBallonAmount());
        technicianDao.setPerKmBallonCost(getPerKmBallonCost());
        technicianDao.setOilPrice(getOilPrice());
        technicianDao.setPerKmOilCost(getPerKmOilCost());
        technicianDao.setAmortization(getAmortization());
        technicianDao.setPerKmAmortization(getPerKmAmortization());
        technicianDao.setAuto(getAuto());
        technicianDao.setPerKmCostAmount(getPerKmCostAmount());
        technicianDao.setStatus(getStatus());
        return technicianDao;
    }
}
