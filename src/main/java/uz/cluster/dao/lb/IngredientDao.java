package uz.cluster.dao.lb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.lb.Ingredient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDao extends BaseDao {

    private int mark;

    private double amount;

    private double sement;

    private double klines;

    private double sheben;

    private double pesok;

    private double antimaroz;

    private double dobavka;

    private double value;

    public Ingredient copy(IngredientDao ingredientDao) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId((int) ingredientDao.getId());
        ingredient.setMark(ingredientDao.getMark());
        ingredient.setAmount(ingredientDao.getAmount());
        ingredient.setPesok(ingredientDao.getPesok());
        ingredient.setSheben(ingredientDao.getSheben());
        ingredient.setKlines(ingredientDao.getKlines());
        ingredient.setDobavka(ingredientDao.getDobavka());
        ingredient.setSement(ingredientDao.getSement());
        ingredient.setAntimaroz(ingredientDao.getAntimaroz());
        ingredient.setValue(ingredientDao.getValue());
        return ingredient;
    }
}
