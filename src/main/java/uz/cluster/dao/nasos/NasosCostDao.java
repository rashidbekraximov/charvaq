package uz.cluster.dao.nasos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.nasos.NasosCost;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.references.model.CostType;
import uz.cluster.entity.references.model.NasosCostType;
import uz.cluster.entity.references.model.ProduceCost;
import uz.cluster.enums.SexEnum;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NasosCostDao extends BaseDao {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private NasosCostType costType;

    private double amount;

    private int costTypeId;

    private SexEnum sexEnum;

    private String description;

    public NasosCost copy(NasosCostDao costDao){
        NasosCost cost = new NasosCost();
        cost.setId((int) costDao.getId());
        cost.setCostType(costDao.getCostType());
        cost.setCostTypeId(costDao.getCostTypeId());
        cost.setDate(costDao.getDate());
        cost.setAmount(costDao.getAmount());
        cost.setDescription(costDao.getDescription());
        return cost;
    }
}
