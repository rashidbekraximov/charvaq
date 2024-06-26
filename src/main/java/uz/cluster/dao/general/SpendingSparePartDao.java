package uz.cluster.dao.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpendingSparePartDao {

    private Long id;

    private LocalDate date;

    private SparePartType sparePartType;

    private FuelType fuelType;

    private double price;

    private Technician technician;

    private ItemEnum item;

    private double qty;

    private double value;

    private int sparePartTypeId;

    private int fuelTypeId;

    private int technicianId;;

    public SpendingSparePart asDao(SpendingSparePartDao dao) {
        SpendingSparePart spendingSparePartDao = new SpendingSparePart();
        spendingSparePartDao.setId(dao.getId());
        spendingSparePartDao.setSparePartType(dao.getSparePartType());
        spendingSparePartDao.setDate(dao.getDate());
        spendingSparePartDao.setSparePartTypeId(dao.getSparePartTypeId());
        spendingSparePartDao.setQty(dao.getQty());
        spendingSparePartDao.setValue(dao.getValue());
        spendingSparePartDao.setPrice(dao.getPrice());
        spendingSparePartDao.setTechnician(dao.getTechnician());
        spendingSparePartDao.setTechnicianId(dao.getTechnicianId());
        spendingSparePartDao.setFuelType(dao.getFuelType());
        spendingSparePartDao.setFuelTypeId(dao.getFuelTypeId());
        spendingSparePartDao.setItem(dao.getItem());
        return spendingSparePartDao;
    }

}
