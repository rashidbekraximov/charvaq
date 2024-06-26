package uz.cluster.dao.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.general.SparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDao {

    private Long id;

    private SparePartType sparePartType;

    private FuelType fuelType;

    private double price;

    private double qty;

    private double value;

    private int sparePartTypeId;

    private ItemEnum item;

    public WarehouseDao copy(WarehouseDao dao) {
        WarehouseDao warehouseDao = new WarehouseDao();
        warehouseDao.setSparePartType(dao.getSparePartType());
        warehouseDao.setSparePartTypeId(dao.getSparePartTypeId());
        warehouseDao.setQty(dao.getQty());
        warehouseDao.setValue(dao.getValue());
        warehouseDao.setPrice(dao.getPrice());
        warehouseDao.setItem(dao.getItem());
        warehouseDao.setFuelType(dao.getFuelType());
        return warehouseDao;
    }
}
