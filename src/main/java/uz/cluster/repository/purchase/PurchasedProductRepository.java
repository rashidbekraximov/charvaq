package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.PurchasedProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct,Integer> {

    void deleteAllByPurchaseId(int purchaseId);

    List<PurchasedProduct> findAllByPurchaseIdOrderByProductType(int purchaseId);

    Optional<PurchasedProduct> findByProductType_IdAndPurchaseId(int productType_id, int purchaseId);

    @Query(value = "select COALESCE(sum(weight),0) from purchased_product  " +
            "where purchase_id = :purchase_id and product_type_id = :product_type_id ", nativeQuery = true)
    List<Double> getSumValueForLineChart(@Param("product_type_id") int productTypeId,@Param("purchase_id") int purchaseId);

}
