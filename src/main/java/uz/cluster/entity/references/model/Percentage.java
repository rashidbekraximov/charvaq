package uz.cluster.entity.references.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.PercentageDao;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "percentage")
public class Percentage {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "direction_id")
    private int directionId;

    @Column(name = "percentage")
    private double percentage;

    public PercentageDao asDao(){
        PercentageDao percentageDao = new PercentageDao();
        percentageDao.setDirectionId(getDirectionId());
        percentageDao.setPercentage(getPercentage());
        return percentageDao;
    }
}
