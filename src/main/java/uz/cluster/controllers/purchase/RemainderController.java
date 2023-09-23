package uz.cluster.controllers.purchase;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.RemainderService;

import java.util.List;


@RestController
@RequestMapping("api/")
@Tag(name = "References Mahsulot qoldiqlari", description = "Mahsulot qoldiqlari")
@RequiredArgsConstructor
public class RemainderController {

    private final RemainderService remainderService;

    @GetMapping("remainders/{status}")
    public ResponseEntity<List<RemainderDao>> getOrderList(@PathVariable String status) {
        return ResponseEntity.ok(remainderService.getRemainderList(MCHJ.valueOf(status.equals("LB") ? "LB" : "CHSM")));
    }

    @GetMapping("remainder/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        RemainderDao remainderDao = remainderService.getById(id);
        if (remainderDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(remainderDao);
    }

    @PostMapping("remainder/save")
    public ResponseEntity<?> save(@RequestBody RemainderDao remainderDao) {
        ApiResponse apiResponse = remainderService.add(remainderDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("remainder/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = remainderService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
