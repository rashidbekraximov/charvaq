package uz.cluster.controllers.references;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cluster.dao.reference.ImageResponse;
import uz.cluster.entity.auth.User;
import uz.cluster.services.auth_service.AuthService;
import uz.cluster.payload.auth.UserDTO;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.util.GlobalParams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    public HttpEntity<?> addUser(MultipartHttpServletRequest request) throws JsonProcessingException {
        MultipartFile file = request.getFile("file");
        String data = request.getParameter("user");

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(data, UserDTO.class);

        ApiResponse apiResponse = null;
        if (userDTO.getId() == 0){
            apiResponse = authService.add(userDTO,file);
        }else {
            apiResponse = authService.edit(userDTO,userDTO.getId(),file);
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

    @GetMapping("/user/image")
    public ImageResponse getImage() throws IOException {
        String fileName = "default.jpg";
        User user = GlobalParams.getCurrentUser();
        // Path to the image file
        File imageFile = new File("/opt/images/" + (user.getFile() == null ? fileName : user.getFile().getFileName()));
        // Read the image file
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        // Encode the image to Base64
        String base64Image = Base64Utils.encodeToString(imageBytes);

        // Create and return the response object
        return new ImageResponse("rasm.jpg", base64Image);
    }
}
