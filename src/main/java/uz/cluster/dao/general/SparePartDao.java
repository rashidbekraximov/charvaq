package uz.cluster.dao.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.general.SparePart;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SparePartDao {

    private Long id;

    private LocalDate date;

    private SparePartType sparePartType;

    private FuelType fuelType;

    private double price;

    private double qty;

    private double value;

    private int sparePartTypeId;

    private int fuelTypeId;

    private ItemEnum item;

    public SparePart copy(SparePartDao dao) {
        SparePart sparePart = new SparePart();
        sparePart.setId(dao.getId());
        sparePart.setSparePartType(dao.getSparePartType());
        sparePart.setSparePartTypeId(dao.getSparePartTypeId());
        sparePart.setFuelType(dao.getFuelType());
        sparePart.setFuelTypeId(dao.getFuelTypeId());
        sparePart.setDate(dao.getDate());
        sparePart.setPrice(dao.getPrice());
        sparePart.setQty(dao.getQty());
        sparePart.setValue(dao.getValue());
        sparePart.setItem(dao.getItem());
        return sparePart;
    }
}
