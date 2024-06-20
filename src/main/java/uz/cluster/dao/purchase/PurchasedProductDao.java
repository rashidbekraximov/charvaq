package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.purchase.PurchasedProduct;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.SexEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedProductDao extends BaseDao {

    private ProductType productType;

    private double weight;

    private double price;

    private double value;

    private int productTypeId;

    private SexEnum sexEnum;

    public PurchasedProduct copy(PurchasedProductDao purchasedProductDao){
        PurchasedProduct price = new PurchasedProduct();
        price.setId((int) purchasedProductDao.getId());
        price.setProductType(purchasedProductDao.getProductType());
        price.setProductTypeId(purchasedProductDao.getProductTypeId());
        price.setPrice(purchasedProductDao.getPrice());
        price.setWeight(purchasedProductDao.getWeight());
        price.setValue(purchasedProductDao.getValue());
        price.setSexEnum(purchasedProductDao.getSexEnum());
        return price;
    }
}