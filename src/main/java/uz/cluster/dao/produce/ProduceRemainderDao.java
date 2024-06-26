package uz.cluster.dao.produce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.produce.Cost;
import uz.cluster.entity.produce.ProduceRemainder;
import uz.cluster.entity.references.model.ProductForProduce;
import uz.cluster.enums.SexEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProduceRemainderDao extends BaseDao {

    private ProductForProduce productForProduce;

    private double amount;

    private int productForProduceId;

    private SexEnum sexEnum;

    public ProduceRemainder copy(ProduceRemainderDao costDao){
        ProduceRemainder cost = new ProduceRemainder();
        cost.setId(costDao.getId());
        cost.setProductForProduce(costDao.getProductForProduce());
        cost.setProductForProduceId(costDao.getProductForProduceId());
        cost.setAmount(costDao.getAmount());
        cost.setSexEnum(costDao.getSexEnum());
        return cost;
    }
}
