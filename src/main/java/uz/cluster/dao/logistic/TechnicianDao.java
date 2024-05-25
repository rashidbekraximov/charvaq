package uz.cluster.dao.logistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.TechniqueType;
import uz.cluster.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianDao extends BaseDao {

    private TechniqueType techniqueType;

    private String autoNumber;

    private double perKmCostAmount;

    private Direction direction;

    private double gasPrice;

    private double perKmGasCost;

    private int ballon;

    private double ballonAmount;

    private double perKmBallonCost;

    private double oilPrice;

    private double perKmOilCost;

    private double amortization;

    private double perKmAmortization;

    private double perKmSalaryAmount;

    private Status status;

    private int techniqueTypeId;

    private int employeeId;

    private int directionId;


    public Technician copy(TechnicianDao technicianDao){
        Technician technician = new Technician();
        technician.setId((int) technicianDao.getId());
        technician.setTechniqueType(technicianDao.getTechniqueType());
        technician.setTechniqueTypeId(technicianDao.getTechniqueTypeId());
        technician.setEmployeeId(technicianDao.getEmployeeId());
        technician.setDirection(technicianDao.getDirection());
        technician.setAutoNumber(technicianDao.getAutoNumber());
        technician.setGasPrice(technicianDao.getGasPrice());
        technician.setPerKmGasCost(technicianDao.getPerKmGasCost());
        technician.setBallon(technicianDao.getBallon());
        technician.setBallonAmount(technicianDao.getBallonAmount());
        technician.setPerKmBallonCost(technicianDao.getPerKmBallonCost());
        technician.setOilPrice(technicianDao.getOilPrice());
        technician.setPerKmOilCost(technicianDao.getPerKmOilCost());
        technician.setAmortization(technicianDao.getAmortization());
        technician.setPerKmAmortization(getPerKmAmortization());
        technician.setPerKmSalaryAmount(getPerKmSalaryAmount());
        technician.setDirectionId(technicianDao.getDirectionId());
        technician.setPerKmCostAmount(technicianDao.getPerKmCostAmount());
        technician.setStatus(technicianDao.getStatus());
        return technician;
    }
}
