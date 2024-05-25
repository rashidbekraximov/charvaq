package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.OrderedProduct;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderedProduct,Long> {


    @Modifying
    @Query(value = "delete from ordered_product where id = :purchaseId",nativeQuery = true)
    void deleteAllByPurchaseId(@Param("purchaseId") int purchaseId);

    List<OrderedProduct> findAllByPurchaseId(int purchaseId);

}