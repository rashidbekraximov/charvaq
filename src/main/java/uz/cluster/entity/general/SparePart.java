package uz.cluster.entity.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;
import uz.cluster.enums.SexEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spare_part")
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "spare_part_type_id")
    private SparePartType sparePartType;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    @Column(name = "price")
    private double price;

    @Column(name = "qty")
    private double qty;

    @Column(name = "value")
    private double value;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "item", columnDefinition = "varchar(20) default 'FUEL'")
    private ItemEnum item;

    @Transient
    private int sparePartTypeId;

    @Transient
    private int fuelTypeId;

    public SparePartDao asDao(){
        SparePartDao sparePartDao = new SparePartDao();
        sparePartDao.setId(getId());
        sparePartDao.setSparePartType(getSparePartType());
        sparePartDao.setSparePartTypeId(getSparePartTypeId());
        sparePartDao.setFuelType(getFuelType());
        sparePartDao.setFuelTypeId(getFuelTypeId());
        sparePartDao.setDate(getDate());
        sparePartDao.setPrice(getPrice());
        sparePartDao.setQty(getQty());
        sparePartDao.setValue(getValue());
        sparePartDao.setItem(getItem());
        return sparePartDao;
    }
}
