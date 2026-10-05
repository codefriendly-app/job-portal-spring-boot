package job_portat.demo;

import job_portat.demo.ResponseDto.UserResponseDto;
import job_portat.demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @Test
    void getUserIfExcited(){
        // Arrange
         UserResponseDto user = new UserResponseDto();
         user.setUserName("Alex");
         when(userService.fetchUser(1l))
                 .thenReturn(user);
         // Act
        try {
            mockMvc.perform(get("/user/id/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userName").value("Alex"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
