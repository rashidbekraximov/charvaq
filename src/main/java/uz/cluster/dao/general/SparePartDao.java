package uz.cluster.dao.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.general.SparePart;
import uz.cluster.entity.references.model.SparePartType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SparePartDao {

    private Long id;

    private LocalDate date;

    private SparePartType sparePartType;

    private double price;

    private double qty;

    private double value;

    private int sparePartTypeId;

    public SparePart copy(SparePartDao dao) {
        SparePart sparePart = new SparePart();
        sparePart.setId(dao.getId());
        sparePart.setSparePartType(dao.getSparePartType());
        sparePart.setSparePartTypeId(dao.getSparePartTypeId());
        sparePart.setDate(dao.getDate());
        sparePart.setPrice(dao.getPrice());
        sparePart.setQty(dao.getQty());
        sparePart.setValue(dao.getValue());
        return sparePart;
    }
}
