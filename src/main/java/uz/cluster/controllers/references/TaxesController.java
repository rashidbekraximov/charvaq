package uz.cluster.controllers.references;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.reference.TaxesDao;
import uz.cluster.entity.references.model.Taxes;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.references_service.TaxService;

@RestController
@RequestMapping("api/references")
@RequiredArgsConstructor
@Tag(name = "References Soliqlar", description = "Spravichniy tablitsalar ustida amallar")
public class TaxesController {

    private final TaxService taxService;

    @GetMapping("/taxes")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(taxService.getTaxList());
    }


    @GetMapping("/tax-type/{id}")
    public ResponseEntity<?> getByTaxTypeId(@PathVariable(value = "id") Integer id) {
        Taxes tax = taxService.getByTaxTypeAndClusterId(id);
        if (tax == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tax);
    }

    @GetMapping("/tax/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Integer id) {
        TaxesDao tax = taxService.getById(id);
        if (tax == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tax);
    }

    @PostMapping("/tax/save")
    public ResponseEntity<ApiResponse> add(@RequestBody TaxesDao taxes) {
        ApiResponse apiResponse = taxService.add(taxes);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
