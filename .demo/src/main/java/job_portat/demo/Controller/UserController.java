package job_portat.demo.Controller;

import jakarta.validation.Valid;
import job_portat.demo.Entity.LoginRequest;
import job_portat.demo.RequestDto.RefreshTokenRequest;
import job_portat.demo.RequestDto.TokenResponse;
import job_portat.demo.RequestDto.UserRequestDto;
import job_portat.demo.ResponseDto.UserResponseDto;
import job_portat.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    public UserController(
            UserService userService
            ){
        this.userService = userService;
    }

    // user Registration
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> registration( @Valid  @RequestBody UserRequestDto requestDto){
        UserResponseDto response = userService.registration(requestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest logRequest){
         TokenResponse response = userService.login(logRequest);
         return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> generateNewToken(@RequestBody RefreshTokenRequest refreshToken){
        TokenResponse response = userService.createNewToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request){
          String response =  userService.logout(request);
          return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDto> fetchUser(@PathVariable Long id){
        UserResponseDto response  = userService.fetchUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
