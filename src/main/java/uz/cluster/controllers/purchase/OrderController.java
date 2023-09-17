package uz.cluster.controllers.purchase;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.OrderDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.OrderService;

import java.util.List;


@RestController
@RequestMapping("api/")
@Tag(name = "References Buyurtmalar", description = "Buyurtmalar")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("orders")
    public ResponseEntity<List<OrderDao>> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("order/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        OrderDao orderDao = orderService.getById(id);
        if (orderDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderDao);
    }

    @GetMapping("order/select")
    public ResponseEntity<?> getForSelect(){
        return ResponseEntity.ok(orderService.getForSelect());
    }


    @PostMapping("order/save")
    public ResponseEntity<?> save(@RequestBody OrderDao orderDao) {
        ApiResponse apiResponse = orderService.add(orderDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("order/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = orderService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
