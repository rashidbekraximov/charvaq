package uz.cluster.controllers.general;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.general.SparePartService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Ehtiyot qismlarni kirim qilish")
@RequiredArgsConstructor
public class SparePartController {

    private final SparePartService sparePartService;

    @GetMapping("spare-parts")
    public ResponseEntity<?> getById() {
        return ResponseEntity.status(HttpStatus.OK).body(sparePartService.getList());
    }

    @GetMapping("spare-part/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        SparePartDao partDao = sparePartService.getById(id);
        if (partDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(partDao);
    }

    @PostMapping("spare-part/save")
    public ResponseEntity<?> save(@RequestBody SparePartDao sparePartDao) {
        ApiResponse apiResponse = sparePartService.add(sparePartDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("spare-part/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = sparePartService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
