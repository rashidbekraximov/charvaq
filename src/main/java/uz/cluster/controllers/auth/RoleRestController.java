package uz.cluster.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.entity.references.model.Form;
import uz.cluster.entity.references.model.Role;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.repository.references.FormRepository;
import uz.cluster.services.auth_service.RoleService;
import uz.cluster.payload.auth.RoleDTO;
import uz.cluster.payload.response.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoleRestController {

    final RoleService roleService;

    final FormRepository formRepository;

    @GetMapping("/role/list")
    public HttpEntity<?> getAll() {
        List<Role> roleList = roleService.getAll();
        return ResponseEntity.ok(roleList);
    }

    @GetMapping("/role-form/{systemRoleName}")
    public HttpEntity<?> getAllByRole(@PathVariable SystemRoleName systemRoleName) {
        List<Form> forms = roleService.getAllFormByRole(systemRoleName);
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/role/{role_id}")
    public HttpEntity<?> getById(@PathVariable(name = "role_id") int id) {
        Role role = roleService.getById(id);
        return ResponseEntity.status(role != null ? 201 : 404).body(role);
    }

    @PostMapping("/role/save")
    public HttpEntity<?> add(@RequestBody RoleDTO roleDTO) {
        ApiResponse apiResponse;
        try {
            apiResponse = roleService.addWithPermission(roleDTO);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = new ApiResponse(false, e.getMessage());
        }

        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);
    }
}
