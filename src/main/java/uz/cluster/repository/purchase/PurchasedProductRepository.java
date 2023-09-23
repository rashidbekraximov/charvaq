package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.PurchasedProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct,Integer> {

    void deleteAllByPurchaseId(int purchaseId);

    List<PurchasedProduct> findAllByPurchaseIdOrderByProductType(int purchaseId);

    Optional<PurchasedProduct> findByProductType_IdAndPurchaseId(int productType_id, int purchaseId);

}
