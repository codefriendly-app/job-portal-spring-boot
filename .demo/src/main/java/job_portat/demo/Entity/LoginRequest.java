package job_portat.demo.Entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username Must be 3-50 character")
    @Size(min = 3,max = 50)
    private String username;
    @NotBlank(message = "Password must be the 6 character")
    @Size(min = 76,max = 100)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
