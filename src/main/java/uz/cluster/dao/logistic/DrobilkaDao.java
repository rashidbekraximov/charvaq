package uz.cluster.dao.logistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.logistic.Drobilka;
import uz.cluster.entity.references.model.DrobilkaType;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrobilkaDao extends BaseDao {

    private ProductType productType;

    private DrobilkaType drobilkaType;

    private Unit unit;

    private double amount;

    private int productTypeId;

    private int drobilkaTypeId;

    private int unitId;

    public Drobilka copy(DrobilkaDao drobilkaDao){
        Drobilka drobilka = new Drobilka();
        drobilka.setId((int) drobilkaDao.getId());
        drobilka.setUnit(drobilkaDao.getUnit());
        drobilka.setUnitId(drobilkaDao.getUnitId());
        drobilka.setProductType(drobilkaDao.getProductType());
        drobilka.setProductTypeId(drobilkaDao.getProductTypeId());
        drobilka.setDrobilkaType(drobilkaDao.getDrobilkaType());
        drobilka.setDrobilkaTypeId(drobilkaDao.getDrobilkaTypeId());
        drobilka.setAmount(drobilkaDao.getAmount());
        return drobilka;
    }
}

