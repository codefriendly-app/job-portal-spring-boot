package job_portat.demo.Controller;

import job_portat.demo.ResponseDto.UserResponseDto;
import job_portat.demo.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getUser(Pageable pageable) {
        Page<UserResponseDto> response =  userService.getUserById(pageable);
        return
                ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String message = userService.deleteUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/specification")
    public ResponseEntity<List<UserResponseDto>> searchUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email ){

        List<UserResponseDto> responseDto = userService.searchUser(username,email);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
