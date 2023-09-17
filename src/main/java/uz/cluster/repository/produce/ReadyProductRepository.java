package uz.cluster.repository.produce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.produce.ReadyProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadyProductRepository extends JpaRepository<ReadyProduct,Integer> {

    Optional<ReadyProduct> findByProductType_Id(int productType_id);

    @Query(value = "select product_type_id productTypeId, COALESCE(sum(amount),0) amount from ready_product " +
            "group by product_type_id", nativeQuery = true)
    List<ProduceCostDao> getAllByProductTypeId();

    @Query(value = "select COALESCE(sum(amount),0) amount from ready_product " +
            "where product_type_id = :product_type_id ", nativeQuery = true)
    List<Double> getAllAmountByProductTypeId(@Param("product_type_id") int productTypeId);
}
