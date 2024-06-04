package uz.cluster.controllers.nasos;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.nasos.NasosService;

@RestController
@RequestMapping("api/")
@Tag(name = "Nasos", description = "Nasos")
@RequiredArgsConstructor
public class NasosController {

    private final NasosService nasosService;

    @GetMapping("nasos")
    public ResponseEntity<?> getOrderList() {
        return ResponseEntity.ok(nasosService.getNasos());
    }

    @PostMapping("nasos/save")
    public ResponseEntity<?> save(@RequestBody Nasos nasos) {
        ApiResponse apiResponse = nasosService.add(nasos);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    
}

