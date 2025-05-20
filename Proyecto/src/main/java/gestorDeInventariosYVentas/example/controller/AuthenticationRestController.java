package gestorDeInventariosYVentas.example.controller;

import gestorDeInventariosYVentas.example.controller.controllerDoc.AuthenticationRestControllerDoc;
import gestorDeInventariosYVentas.example.dto.input.AuthCreateUserRequest;
import gestorDeInventariosYVentas.example.dto.input.AuthLoginRequest;
import gestorDeInventariosYVentas.example.dto.output.AuthResponse;
import gestorDeInventariosYVentas.example.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag( name = "Authentication RestController", description = "Controller for authentication")
public class AuthenticationRestController implements AuthenticationRestControllerDoc {

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody  AuthCreateUserRequest authCreateUser){

        return new ResponseEntity<>(this.userDetailService.createUser(authCreateUser), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest){

        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }
}
