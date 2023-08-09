package uz.cluster.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.entity.auth.User;
import uz.cluster.security.JwtResponse;
import uz.cluster.services.auth_service.AuthService;
import uz.cluster.payload.auth.LoginDTO;
import uz.cluster.util.GlobalParams;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController  {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDTO loginDTO){
        JwtResponse jwtResponse = authService.login(loginDTO);
        return ResponseEntity.status(jwtResponse.isSuccess() ? 200 : 403).body(jwtResponse);
    }

    @GetMapping("/api/account")
    public HttpEntity<?> retriveAccount(){
        User user = GlobalParams.getCurrentUser();
        return ResponseEntity.status(user.getId() != 0 ? 200 : 403).body(user);
    }
}