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
import uz.cluster.enums.SexEnum;

import javax.persistence.Column;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadyProductDao extends BaseDao {

    private ProductType productType;

    private double amount;

    private double costAmount;

    private int productTypeId;

    private double costPerKgSementAmount;

    private SexEnum sexEnum;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public ReadyProduct copy(ReadyProductDao remainderDao){
        ReadyProduct remainder = new ReadyProduct();
        remainder.setId((int) remainderDao.getId());
        remainder.setProductType(remainderDao.getProductType());
        remainder.setProductTypeId(remainderDao.getProductTypeId());
        remainder.setAmount(remainderDao.getAmount());
        remainder.setCostAmount(remainderDao.getCostAmount());
        remainder.setCostPerKgSementAmount(remainderDao.getCostPerKgSementAmount());
        remainder.setDate(remainderDao.getDate());
        remainder.setSexEnum(remainderDao.getSexEnum());
        return remainder;
    }
}
