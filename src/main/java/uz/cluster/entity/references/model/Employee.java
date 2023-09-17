package uz.cluster.entity.references.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.reference.EmployeeDao;
import uz.cluster.entity.Auditable;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "employee")
public class Employee extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "per_hour_wage_amount")
    private double perHourWageAmount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Transient
    List<Percentage> percentages = new ArrayList<>();

    @Transient
    private int positionId;

    public EmployeeDao asDao(){
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.setId(getId());
        employeeDao.setName(getName());
        employeeDao.setPositionId(getPositionId());
        employeeDao.setPosition(getPosition());
        employeeDao.setDirection(getDirection());
        employeeDao.setPerHourWageAmount(getPerHourWageAmount());
        employeeDao.setStatus(getStatus());
        for (Percentage percentage : getPercentages()){
            employeeDao.getPercentages().add(percentage.asDao());
        }
        return employeeDao;
    }
}
