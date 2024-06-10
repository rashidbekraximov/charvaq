package uz.cluster.entity.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.dao.general.WarehouseDao;
import uz.cluster.entity.references.model.SparePartType;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spare_part_type_id")
    private SparePartType sparePartType;

    @Column(name = "qty")
    private double qty;

    @Column(name = "value")
    private double value;

    @Column(name = "price")
    private double price;

    @Transient
    private int sparePartTypeId;

    public WarehouseDao asDao() {
        WarehouseDao warehouseDao = new WarehouseDao();
        warehouseDao.setId(getId());
        warehouseDao.setSparePartType(getSparePartType());
        warehouseDao.setSparePartTypeId(getSparePartTypeId());
        warehouseDao.setQty(getQty());
        warehouseDao.setValue(getValue());
        warehouseDao.setPrice(getPrice());
        return warehouseDao;
    }
}
