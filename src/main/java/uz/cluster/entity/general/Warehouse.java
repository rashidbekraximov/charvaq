package uz.cluster.entity.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.dao.general.WarehouseDao;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;
import uz.cluster.enums.MCHJ;

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

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    @Column(name = "qty")
    private double qty;

    @Column(name = "value")
    private double value;

    @Column(name = "price")
    private double price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "item", columnDefinition = "varchar(20) default 'FUEL'")
    private ItemEnum item;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "mchj", columnDefinition = "varchar(20) default 'LB'")
    private MCHJ mchj;

    @Transient
    private int sparePartTypeId;

    @Transient
    private int fuelTypeId;

    public WarehouseDao asDao() {
        WarehouseDao warehouseDao = new WarehouseDao();
        warehouseDao.setId(getId());
        warehouseDao.setSparePartType(getSparePartType());
        warehouseDao.setSparePartTypeId(getSparePartTypeId());
        warehouseDao.setQty(getQty());
        warehouseDao.setValue(getValue());
        warehouseDao.setPrice(getPrice());
        warehouseDao.setItem(getItem());
        warehouseDao.setFuelType(getFuelType());
        return warehouseDao;
    }
}
