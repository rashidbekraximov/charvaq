package uz.cluster.dao.lb;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MixerDao extends BaseDao {

    private String auto;

    private double perKmCostAmount;

    private double gasPrice;

    private double perKmGasCost;

    private int ballon;

    private double ballonAmount;

    private double perKmBallonCost;

    private double oilPrice;

    private double perKmOilCost;

    private double amortization;

    private double perKmAmortization;

    private Status status;

    public Mixer copy(MixerDao technicianDao){
        Mixer technician = new Mixer();
        technician.setId((int) technicianDao.getId());
        technician.setAuto(technicianDao.getAuto());
        technician.setGasPrice(technicianDao.getGasPrice());
        technician.setPerKmGasCost(technicianDao.getPerKmGasCost());
        technician.setBallon(technicianDao.getBallon());
        technician.setBallonAmount(technicianDao.getBallonAmount());
        technician.setPerKmBallonCost(technicianDao.getPerKmBallonCost());
        technician.setOilPrice(technicianDao.getOilPrice());
        technician.setPerKmOilCost(technicianDao.getPerKmOilCost());
        technician.setAmortization(technicianDao.getAmortization());
        technician.setPerKmAmortization(getPerKmAmortization());
        technician.setPerKmCostAmount(technicianDao.getPerKmCostAmount());
        technician.setStatus(technicianDao.getStatus());
        return technician;
    }
}
