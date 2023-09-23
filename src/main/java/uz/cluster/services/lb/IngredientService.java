package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.IngredientDao;
import uz.cluster.entity.lb.Ingredient;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.IngredientRepository;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_VIEW)
    public List<IngredientDao> getList() {
        return ingredientRepository.findAll().stream().map(Ingredient::asDao).collect(Collectors.toList());
    }

    public IngredientDao getById(int id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isEmpty()) {
            return null;
        } else {
            return optionalIngredient.get().asDao();
        }
    }

    public IngredientDao getByAmountAndMark(int mark,double amount) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByAmountAndMark(amount,mark);
        if (optionalIngredient.isEmpty()) {
            return null;
        } else {
            return optionalIngredient.get().asDao();
        }
    }

    public Map<Integer,Double> getAmountHtmlList(){
        Map<Integer,Double> htmlOption = new HashMap<>();
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient ingredient : ingredients) {
            htmlOption.put(ingredient.getId(), ingredient.getAmount());
        }
        return htmlOption;
    }

    public Map<Integer,Integer> getMarkHtmlList(){
        Map<Integer,Integer> htmlOption = new HashMap<>();
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient ingredient : ingredients) {
            htmlOption.put(ingredient.getId(), ingredient.getMark());
        }
        return htmlOption;
    }

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(IngredientDao ingredientDao) {
        Ingredient ingredient = ingredientDao.copy(ingredientDao);

        if (ingredient.getId() != 0) {
            return edit(ingredient);
        }

        Optional<Ingredient> optionalIngredient = ingredientRepository.findByAmountAndMark(ingredient.getAmount(),ingredient.getMark());

        if (optionalIngredient.isPresent()){
            return new ApiResponse(true, LanguageManager.getLangMessage("already_created"));
        }

        Ingredient saved = ingredientRepository.save(ingredient);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Ingredient ingredient) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredient.getId());

        if (optionalIngredient.isPresent()){
            Ingredient edited = ingredientRepository.save(ingredient);
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isPresent()){
            ingredientRepository.deleteById(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
