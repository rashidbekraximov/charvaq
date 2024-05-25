package uz.cluster.controllers.produce;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.produce.ProduceRemainderDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.produce.ProduceRemainderService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Ishlab chiqarish ostatka", description = "Mahsulot qoldiqlari")
@RequiredArgsConstructor
public class ProduceRemainderController {

    private final ProduceRemainderService produceRemainderService;

    @GetMapping("produce-remainders")
    public ResponseEntity<?> getById() {
        return ResponseEntity.status(HttpStatus.OK).body(produceRemainderService.getProduceRemainderList());
    }

    @GetMapping("produce-remainder/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        ProduceRemainderDao remainderDao = produceRemainderService.getById(id);
        if (remainderDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(remainderDao);
    }

    @PostMapping("produce-remainder/save")
    public ResponseEntity<?> save(@RequestBody ProduceRemainderDao remainderDao) {
        ApiResponse apiResponse = produceRemainderService.add(remainderDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("produce-remainder/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = produceRemainderService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
