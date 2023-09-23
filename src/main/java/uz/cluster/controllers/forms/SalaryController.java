package uz.cluster.controllers.forms;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.salary.SalaryDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.salary.SalaryService;

import java.sql.Date;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Tag(name = "Ish haqi", description = "Ish haqi")
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping("salary/list")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(salaryService.getList());
    }

    @GetMapping("salary/tabel/{beginDate}/{endDate}/{id}")
    public ResponseEntity<?> getEmployeeList(@PathVariable String beginDate,@PathVariable String endDate,@PathVariable int id){
        return ResponseEntity.ok(salaryService.getEmployeeList(Date.valueOf(beginDate),Date.valueOf(endDate),id));
    }

    @GetMapping("salary/header/{beginDate}/{endDate}/{id}")
    public ResponseEntity<?> getHeaderList(@PathVariable String beginDate,@PathVariable String endDate, @PathVariable int id){
        return ResponseEntity.ok(salaryService.getHeaderList(Date.valueOf(beginDate),Date.valueOf(endDate),id));
    }

    @GetMapping("salary/table/{id}")
    public ResponseEntity<?> getTableList(@PathVariable int id){
        return ResponseEntity.ok(salaryService.getTableList(id));
    }

    @PostMapping("salary/add")
    public ResponseEntity<?> add(@RequestBody SalaryDao salaryDao){
        ApiResponse apiResponse = salaryService.add(salaryDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("salary/edit")
    public ResponseEntity<?> edit(@RequestBody SalaryDao salaryDao){
        ApiResponse apiResponse = salaryService.edit(salaryDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("salary/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        return ResponseEntity.ok(salaryService.delete(id));
    }
}
