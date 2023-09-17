package uz.cluster.entity.logistic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.logistic.TechnicianDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.TechniqueType;
import uz.cluster.enums.Status;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "technician")
public class Technician extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "technique_type_id")
    private TechniqueType techniqueType;

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

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Transient
    private int techniqueTypeId;

    @Transient
    private int employeeId;

    @Transient
    private int directionId;

    public TechnicianDao asDao(){
        TechnicianDao technicianDao = new TechnicianDao();
        technicianDao.setId(getId());
        technicianDao.setTechniqueType(getTechniqueType());
        technicianDao.setTechniqueTypeId(getTechniqueTypeId());
        technicianDao.setEmployee(getEmployee());
        technicianDao.setEmployeeId(getEmployeeId());
        technicianDao.setDirection(getDirection());
        technicianDao.setAutoNumber(getAutoNumber());
        technicianDao.setGasPrice(getGasPrice());
        technicianDao.setPerKmGasCost(getPerKmGasCost());
        technicianDao.setBallon(getBallon());
        technicianDao.setBallonAmount(getBallonAmount());
        technicianDao.setPerKmBallonCost(getPerKmBallonCost());
        technicianDao.setOilPrice(getOilPrice());
        technicianDao.setPerKmOilCost(getPerKmOilCost());
        technicianDao.setAmortization(getAmortization());
        technicianDao.setPerKmAmortization(getPerKmAmortization());
        technicianDao.setDirectionId(getDirectionId());
        technicianDao.setPerKmCostAmount(getPerKmCostAmount());
        technicianDao.setStatus(getStatus());
        return technicianDao;
    }
}
