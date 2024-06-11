package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_VIEW)
    public List<IngredientDao> getList() {
        log.info("Hammasi chiqarildi !");
        return ingredientRepository.findAll().stream().map(Ingredient::asDao).collect(Collectors.toList());
    }

    public IngredientDao getById(int id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isEmpty()) {
            log.error("Bu Id " + id + " haqida ma'lumot topilmadi !");
            return null;
        } else {
            log.info("Bu Id " + id + " bo'yicha malumot chiqarildi !");
            return optionalIngredient.get().asDao();
        }
    }

    public IngredientDao getByAmountAndMark(int mark,double amount) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByAmountAndMark(amount,mark);
        if (optionalIngredient.isEmpty()) {
            log.error("Malumot topilmadi !");
            return null;
        } else {
            log.info(" :) ");
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
            log.info("Malumot yangilandi !");
            return edit(ingredient);
        }

        Optional<Ingredient> optionalIngredient = ingredientRepository.findByAmountAndMark(ingredient.getAmount(),ingredient.getMark());

        if (optionalIngredient.isPresent()){
            log.info("Malumot allaqachon yaratilgan !");
            return new ApiResponse(true, LanguageManager.getLangMessage("already_created"));
        }

        Ingredient saved = ingredientRepository.save(ingredient);
        log.info("Ma'lumot saqlandi !");
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Ingredient ingredient) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredient.getId());

        if (optionalIngredient.isPresent()){
            Ingredient edited = ingredientRepository.save(ingredient);
            log.info("Ma'lumot tahrirlandi :)");
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            log.error("Ma'lumotni tahrirlab bo'lmadi :(");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.INGREDIENT, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isPresent()){
            ingredientRepository.deleteById(id);
            log.info("Bu Id " + id + " O'chirildi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            log.error("Bu Id " + id + " O'chirib bo'lmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
