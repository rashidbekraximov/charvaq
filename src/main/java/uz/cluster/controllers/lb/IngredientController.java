package uz.cluster.controllers.lb;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.lb.IngredientDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.lb.IngredientService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References Ingredients", description = "Tarkibi")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("ingredients")
    public ResponseEntity<List<IngredientDao>> getList() {
        return ResponseEntity.ok(ingredientService.getList());
    }

    @GetMapping("ingredient/marks")
    public ResponseEntity<?> getMarkList() {
        return ResponseEntity.ok(ingredientService.getMarkHtmlList());
    }

    @GetMapping("ingredient/select/{mark}/{amount}")
    public ResponseEntity<?> getListForSelect(@PathVariable int mark,@PathVariable double amount) {
        return ResponseEntity.ok(ingredientService.getByAmountAndMark(mark, amount));
    }

    @GetMapping("ingredient/amounts")
    public ResponseEntity<?> getAmountList() {
        return ResponseEntity.ok(ingredientService.getAmountHtmlList());
    }

    @GetMapping("ingredient/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        IngredientDao dao = ingredientService.getById(id);
        if (dao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dao);
    }

    @PostMapping("ingredient/save")
    public ResponseEntity<?> save(@RequestBody IngredientDao dao) {
        ApiResponse apiResponse = ingredientService.add(dao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("ingredient/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = ingredientService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

