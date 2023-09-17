package uz.cluster.controllers.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.logistic.TechnicianDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.logistic.TechnicianService;
import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References Texnikalar", description = "Texnikalar")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @GetMapping("technicians")
    public ResponseEntity<List<TechnicianDao>> getTechnicianList(){
        return ResponseEntity.ok(technicianService.getTechnicianList());
    }

    @GetMapping("technician/select")
    public ResponseEntity<?> getTechnicianForSelect(){
        return ResponseEntity.ok(technicianService.getTechnicianForSelect());
    }

    @GetMapping("technician/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        TechnicianDao technicianDao = technicianService.getById(id);
        if (technicianDao == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(technicianDao);
    }

    @PostMapping("technician/save")
    public ResponseEntity<?> save(@RequestBody TechnicianDao technicianDao){
        ApiResponse apiResponse = technicianService.add(technicianDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("technician/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id){
        ApiResponse apiResponse = technicianService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
