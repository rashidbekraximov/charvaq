package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.PurchasedProduct;

import java.util.List;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct,Integer> {

    void deleteAllByPurchaseId(int purchaseId);

    List<PurchasedProduct> findAllByPurchaseIdOrderByProductType(int purchaseId);

}
