package uz.cluster.controllers.references;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cluster.services.auth_service.AuthService;
import uz.cluster.payload.auth.UserDTO;
import uz.cluster.payload.response.ApiResponse;

/*
 * User bo'yicha controller
 */
@RestController
@RequestMapping(path = "/api/")
@Tag(name = "Users", description = "User Ustida amallar")
public class UserController {
    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Userlar ro'yxati")
    @GetMapping("users")
    public HttpEntity<?> getAll() {
        return new ResponseEntity<>(authService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Yangi Userni kiritish")
    @PostMapping("user/save")
    public HttpEntity<?> addUser(@RequestBody UserDTO userDTO) {
        ApiResponse apiResponse = null;
        if (userDTO.getId() == 0){
            apiResponse = authService.add(userDTO);
        }else {
            apiResponse = authService.edit(userDTO,userDTO.getId());
        }
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @Operation(summary = "Userni o'chirib tashlash")
    @DeleteMapping(path = "user/{user_id}")
    public HttpEntity<?> deleteUser(@PathVariable("user_id") int userId) {
        ApiResponse apiResponse = authService.delete(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @Operation(summary = "User rolini unique orqali olish")
    @GetMapping(path = "user/{user_id}")
    public HttpEntity<?> getById(@PathVariable("user_id") int userId) {
        return new ResponseEntity<>(authService.getById(userId), HttpStatus.OK);
    }
}
