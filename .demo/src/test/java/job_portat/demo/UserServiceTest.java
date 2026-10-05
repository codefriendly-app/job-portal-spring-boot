package job_portat.demo;
import job_portat.demo.Entity.User;
import job_portat.demo.Repository.UserRepository;
import job_portat.demo.ResponseDto.UserResponseDto;
import job_portat.demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    org.springframework.data.domain.Pageable pageable = PageRequest.of(0, 10);
    @Test
    void testGetUserById_ReturnsPagedUserResponseDto() {
        // Arrange dummy input pageable
        org.springframework.data.domain.Pageable pageable = PageRequest.of(0, 10);

        // Arrange dummy user input
        User user = new User();
        user.setId(1L);
        user.setUsername("Prasad");
        List<User> userlist = List.of(user);
        Page<User> dummyUserPage = new PageImpl<>(userlist, pageable, userlist.size());

        // action
        when(userRepository.findAll(any(Pageable.class))).thenReturn(dummyUserPage);

        //assert

        Page<UserResponseDto> result = userService.getUserById(pageable);

        UserResponseDto userResponseDto = result.getContent().get(0);
        assertEquals("Prasad", userResponseDto.getUserName());

        //verify
        verify(userRepository,times(1)).findAll(pageable);
    }
    @Test
    void ExceptionThrowsWhenUserNotFound(){
         //arrange
        Pageable pageable1 = PageRequest.of(0,10);
        when(userRepository.findAll(pageable1))
                .thenThrow(new RuntimeException("User not found"));
        // act
        RuntimeException exception = assertThrows(RuntimeException.class,()->
            userService.getUserById(pageable1)
        );
        assertEquals("User not found",exception.getMessage());
    }
}
