package uz.cluster.entity.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.entity.references.model.SparePartType;

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

    @Column(name = "price")
    private double price;

    @Column(name = "qty")
    private double qty;

    @Column(name = "value")
    private double value;

    @Transient
    private int sparePartTypeId;

    public SparePartDao asDao(){
        SparePartDao sparePartDao = new SparePartDao();
        sparePartDao.setId(getId());
        sparePartDao.setSparePartType(getSparePartType());
        sparePartDao.setSparePartTypeId(getSparePartTypeId());
        sparePartDao.setDate(getDate());
        sparePartDao.setPrice(getPrice());
        sparePartDao.setQty(getQty());
        sparePartDao.setValue(getValue());
        return sparePartDao;
    }
}
