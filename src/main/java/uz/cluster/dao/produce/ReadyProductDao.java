package uz.cluster.dao.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;

import javax.persistence.Column;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadyProductDao extends BaseDao {

    private ProductType productType;

    private Unit unit;

    private double amount;

    private double costAmount;

    private int productTypeId;

    private int unitId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public ReadyProduct copy(ReadyProductDao remainderDao){
        ReadyProduct remainder = new ReadyProduct();
        remainder.setId((int) remainderDao.getId());
        remainder.setUnit(remainderDao.getUnit());
        remainder.setUnitId(remainderDao.getUnitId());
        remainder.setProductType(remainderDao.getProductType());
        remainder.setProductTypeId(remainderDao.getProductTypeId());
        remainder.setAmount(remainderDao.getAmount());
        remainder.setCostAmount(remainderDao.getCostAmount());
        remainder.setDate(remainderDao.getDate());
        return remainder;
    }
}
