package uz.cluster.entity.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.dao.produce.ReadyProductDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.CostType;
import uz.cluster.entity.references.model.ProduceCost;
import uz.cluster.enums.SexEnum;
import uz.cluster.enums.Status;
import uz.cluster.enums.produce.ProduceEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "cost")
public class Cost  extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "cost_type_id")
    private ProduceCost costType;

    @Column(name = "amount")
    private double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "spending_type_id")
    private int spendingTypeId = ProduceEnum.REAL_COST.getValue();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sex", columnDefinition = "varchar(5) default 'SEX1'")
    private SexEnum sexEnum;

    @Transient
    private int costTypeId;

    public CostDao asDao(){
        CostDao costDao = new CostDao();
        costDao.setId(getId());
        costDao.setCostType(getCostType());
        costDao.setCostTypeId(getCostTypeId());
        costDao.setDate(getDate());
        costDao.setAmount(getAmount());
        costDao.setSexEnum(getSexEnum());
        costDao.setDescription(getDescription());
        return costDao;
    }
}
