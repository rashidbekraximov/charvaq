package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.purchase.OrderedProduct;
import uz.cluster.entity.references.model.ProductType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDao extends BaseDao {

    private ProductType productType;

    private double weight;

    private double price;

    private double value;

    private int productTypeId;

    public OrderedProduct copy(OrderProductDao purchasedProductDao){
        OrderedProduct price = new OrderedProduct();
        price.setId((int) purchasedProductDao.getId());
        price.setProductType(purchasedProductDao.getProductType());
        price.setProductTypeId(purchasedProductDao.getProductTypeId());
        price.setPrice(purchasedProductDao.getPrice());
        price.setWeight(purchasedProductDao.getWeight());
        price.setValue(purchasedProductDao.getValue());
        return price;
    }
}
