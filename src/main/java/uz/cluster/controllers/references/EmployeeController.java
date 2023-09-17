package uz.cluster.controllers.references;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.reference.EmployeeDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.salary.EmployeeService;

import java.util.List;


@RestController
@RequestMapping("api/")
@Tag(name = "References Ishchilar", description = "Ishchilar")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("workers")
    public ResponseEntity<List<EmployeeDao>> getEmployeeList(){
        return ResponseEntity.ok(employeeService.getEmployeeList());
    }

    @GetMapping("worker/select")
    public ResponseEntity<?> getEmployeeForSelect(){
        return ResponseEntity.ok(employeeService.getEmployeeForSelect());
    }

    @GetMapping("worker/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        EmployeeDao employeeDao = employeeService.getById(id);
        if (employeeDao == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeDao);
    }

    @PostMapping("worker/save")
    public ResponseEntity<?> save(@RequestBody EmployeeDao employeeDao){
        ApiResponse apiResponse = employeeService.add(employeeDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("worker/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id){
        ApiResponse apiResponse = employeeService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
