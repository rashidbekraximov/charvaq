package uz.cluster.dao.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.entity.references.model.CostType;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostDao extends BaseDao {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private CostType costType;

    private double amount;

    private int costTypeId;

    public Cost copy(CostDao costDao){
        Cost cost = new Cost();
        cost.setId((int) costDao.getId());
        cost.setCostType(costDao.getCostType());
        cost.setCostTypeId(costDao.getCostTypeId());
        cost.setDate(costDao.getDate());
        cost.setAmount(costDao.getAmount());
        return cost;
    }

}
