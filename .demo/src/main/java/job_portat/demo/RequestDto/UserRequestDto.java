package job_portat.demo.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import job_portat.demo.Entity.Role;
import org.springframework.validation.annotation.Validated;


public class UserRequestDto {
    @NotBlank(message = "User field is required ")
    @Size(min = 3,max = 50,message = "username must be 3-50 character  ")
    private String username;
    @NotBlank(message = "email is required ")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6,max = 100,message = "password must be at least 6 character")
    private String password;
    private Role role;
    private String number;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRole(String user) {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
