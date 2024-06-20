package uz.cluster.entity.nasos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.nasos.NasosDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.TechniqueType;
import uz.cluster.enums.NasosStatus;
import uz.cluster.enums.SexEnum;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "nasos")
public class Nasos extends Auditable {

    @Id
    @GeneratedValue(generator = "nasos_sq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "nasos_sq", sequenceName = "nasos_sq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "client")
    private String client;

    @Column(name = "work_hour")
        private double workHour;

    @Column(name = "min_summ")
    private double minPrice;

    @Column(name = "additional_summ")
    private double additionalPrice;

    @Column(name = "total_summ")
    private double totalSumm;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "spending_group")
    private NasosStatus spendingGroup = NasosStatus.SERVICE;

    public NasosDao asDao(){
        NasosDao nasosDao = new NasosDao();
        nasosDao.setId(getId());
        nasosDao.setDate(getDate());
        nasosDao.setClient(getClient());
        nasosDao.setWorkHour(getWorkHour());
        nasosDao.setMinPrice(getMinPrice());
        nasosDao.setAdditionalPrice(getAdditionalPrice());
        nasosDao.setTotalSumm(getTotalSumm());
        nasosDao.setDescription(getDescription());
        return nasosDao;
    }
}
