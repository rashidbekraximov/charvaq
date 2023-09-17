package uz.cluster.entity.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.Position;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Table(name = "salary")
public class Salary extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long id;

    @Hidden
    @Column(name = "document_id", updatable = false)
    private long documentId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "per_hour_wage_amount")
    private double perHourWageAmount;

    @Column(name = "all_hour")
    private int allHour;

    @Column(name = "all_amount")
    private double allAmount;

    @Transient
    private int positionId;

    @Transient
    private int employeeId;
}
