package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.lb.Ingredient;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Integer> {

    Optional<Ingredient> findByAmountAndMark(double amount, int mark);

}
