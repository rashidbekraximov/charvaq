package uz.cluster.dao.lb;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.lb.LBPrice;
import uz.cluster.entity.purchase.Price;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.Status;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LBPriceDao extends BaseDao {

    private ProductType productType;

    private double price;

    private Status status;

    private int productTypeId;

    public LBPrice copy(LBPriceDao priceDao){
        LBPrice price = new LBPrice();
        price.setId((int) priceDao.getId());
        price.setProductType(priceDao.getProductType());
        price.setProductTypeId(priceDao.getProductTypeId());
        price.setPrice(priceDao.getPrice());
        price.setStatus(priceDao.getStatus());
        return price;
    }
}

