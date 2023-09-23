package uz.cluster.entity.lb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.lb.IngredientDao;
import uz.cluster.entity.Auditable;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "ingredient")
public class Ingredient extends Auditable {

    @Id
    @GeneratedValue(generator = "ingredient_sq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ingredient_sq", sequenceName = "ingredient_sq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "mark")
    private int mark;

    @Column(name = "amount")
    private double amount;

    @Column(name = "sement")
    private double sement;

    @Column(name = "klines")
    private double klines;

    @Column(name = "sheben")
    private double sheben;

    @Column(name = "pesok")
    private double pesok;

    @Column(name = "dobavka")
    private double dobavka;

    @Column(name = "value")
    private double value;

    public IngredientDao asDao() {
        IngredientDao ingredientDao = new IngredientDao();
        ingredientDao.setId(getId());
        ingredientDao.setMark(getMark());
        ingredientDao.setAmount(getAmount());
        ingredientDao.setSement(getSement());
        ingredientDao.setKlines(getKlines());
        ingredientDao.setSheben(getSheben());
        ingredientDao.setPesok(getPesok());
        ingredientDao.setDobavka(getDobavka());
        ingredientDao.setValue(getValue());
        return ingredientDao;
    }
}