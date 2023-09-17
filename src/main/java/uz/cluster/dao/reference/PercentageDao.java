package uz.cluster.dao.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.references.model.Percentage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PercentageDao extends BaseDao {

    private int directionId;

    private double percentage;

    public Percentage copy(PercentageDao percentageDao){
        Percentage percentage = new Percentage();
        percentage.setDirectionId(percentageDao.getDirectionId());
        percentage.setPercentage(percentageDao.getPercentage());
        return percentage;
    }
}
