package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemainderDao extends BaseDao {

    private ProductType productType;

    private Unit unit;

    private double amount;

    private MCHJ mchj;

    private int productTypeId;

    private int unitId;

    public Remainder copy(RemainderDao remainderDao){
        Remainder remainder = new Remainder();
        remainder.setId((int) remainderDao.getId());
        remainder.setUnit(remainderDao.getUnit());
        remainder.setUnitId(remainderDao.getUnitId());
        remainder.setProductType(remainderDao.getProductType());
        remainder.setProductTypeId(remainderDao.getProductTypeId());
        remainder.setAmount(remainderDao.getAmount());
        remainder.setMchj(remainderDao.getMchj());
        return remainder;
    }
}

