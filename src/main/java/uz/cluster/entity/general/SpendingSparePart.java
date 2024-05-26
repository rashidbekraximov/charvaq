package uz.cluster.entity.general;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.SparePartType;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spending_spare_part")
public class SpendingSparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name = "spare_part_typ")
    private SparePartType sparePartType;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "value")
    private double value;

    @Column(name = "qty")
    private double qty;

    @Column(name = "price")
    private double price;

    @Transient
    private int sparePartTypeId;

    @Transient
    private int technicianId;

    public SpendingSparePartDao asDao() {
        SpendingSparePartDao spendingSparePartDao = new SpendingSparePartDao();
        spendingSparePartDao.setId(getId());
        spendingSparePartDao.setSparePartType(getSparePartType());
        spendingSparePartDao.setSparePartTypeId(getSparePartTypeId());
        spendingSparePartDao.setQty(getQty());
        spendingSparePartDao.setValue(getValue());
        spendingSparePartDao.setTechnician(getTechnician());
        spendingSparePartDao.setTechnicianId(getTechnicianId());
        spendingSparePartDao.setDate(getDate());
        return spendingSparePartDao;
    }

}
