package uz.cluster.controllers.general;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.general.SpendingSparePartService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Ehtiyot qism sarf")
@RequiredArgsConstructor
public class SpendingSparePartController {


    public final SpendingSparePartService spendingSparePartService;

    @GetMapping("spending-spare-parts")
    public ResponseEntity<?> getById() {
        return ResponseEntity.status(HttpStatus.OK).body(spendingSparePartService.getList());
    }

    @GetMapping("spending-spare-part/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        SpendingSparePartDao partDao = spendingSparePartService.getById(id);
        if (partDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(partDao);
    }

    @PostMapping("spending-spare-part/save")
    public ResponseEntity<?> save(@RequestBody SpendingSparePartDao spendingSparePartDao) {
        ApiResponse apiResponse = spendingSparePartService.add(spendingSparePartDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("spending-spare-part/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = spendingSparePartService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
