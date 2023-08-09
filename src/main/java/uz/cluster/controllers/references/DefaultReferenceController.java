package uz.cluster.controllers.references;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.reference.DefaultReference;
import uz.cluster.services.references_service.DefaultReferenceService;

@RestController
@Tag(name = "O'xshash malumotlar uchun",description = "O'xshash malumotlar")
@RequiredArgsConstructor
@RequestMapping(path = "api/references/")
public class DefaultReferenceController {

    private final DefaultReferenceService defaultReferenceService;


    @Operation(summary = "oddish spravichniylar ro'yxatini olish")
    @GetMapping(value = "/def_references")
    public ResponseEntity<?> getAllReferences() {
        try {
            return ResponseEntity.ok(defaultReferenceService.getReferenceLists());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "oddish spravichniylar ro'yxatini olish")
    @GetMapping("/def_references/{referenceId}")
    public ResponseEntity<?> getAllReferences(@PathVariable(name = "referenceId") int referenceId) {
        if (referenceId == 0 ){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(defaultReferenceService.getReferenceItems(referenceId));
    }

    @Operation(summary = "Default Spravichniyni saqlash")
    @PostMapping("/def_references")
    public ResponseEntity<?> save(@RequestBody DefaultReference reference) {

        try {
            defaultReferenceService.save(reference);
            ReferencesController.clearGlobalCache();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
