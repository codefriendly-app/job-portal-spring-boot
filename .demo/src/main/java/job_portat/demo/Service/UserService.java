package job_portat.demo.Service;

import job_portat.demo.CustomeExceptions.UsernameAlreadyExists;
import job_portat.demo.Entity.LoginRequest;
import job_portat.demo.Entity.RefreshToken;
import job_portat.demo.Entity.Role;
import job_portat.demo.Entity.User;
import job_portat.demo.Repository.RefreshTokenRepository;
import job_portat.demo.Repository.RoleRepository;
import job_portat.demo.Repository.UserRepository;
import job_portat.demo.RequestDto.RefreshTokenRequest;
import job_portat.demo.RequestDto.TokenResponse;
import job_portat.demo.RequestDto.UserRequestDto;
import job_portat.demo.ResponseDto.UserResponseDto;
import job_portat.demo.Specification.UserSpecification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private RefreshTokenRepository refreshTokenRepository;

    public UserService(UserRepository userRepository,
                       UserDetailsService userDetailsService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService,
                       RefreshTokenRepository refreshTokenRepository){

        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserResponseDto registration(UserRequestDto requestDto) {
        if(userRepository.findByUsername(requestDto.getUsername()).isPresent()){
            throw new UsernameAlreadyExists("Username Already Exists");
        }
        User user = toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.findByName("USER").orElseThrow();
        if(role==null){
            role = roleRepository.save(new Role("USER"));
        }
        user.setRole(role);
        User user1 = userRepository.save(user);
        UserResponseDto userResponseDto =toResponse(user1);
        return userResponseDto;
    }
    private UserResponseDto toResponse(User user1) {
        UserResponseDto response = new UserResponseDto();
        response.setUserName(user1.getUsername());
        response.setEmail(user1.getEmail());
        if(user1.getRole()!=null){
            response.setRole(user1.getRole().getName());
        }
        else {
            System.out.println("role not assign ");
        }
        response.setNumber(user1.getNumber());
        response.setPassword(user1.getPassword());
        response.setCreateAt(user1.getCreateAt());
        return response;
    }

    private User toEntity(UserRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setNumber(requestDto.getNumber());
        user.setRole(requestDto.getRole());
        user.setEnable(true);
        user.setCreateAt(new Date(System.currentTimeMillis()));
        return user;
    }

    public TokenResponse login(LoginRequest logRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logRequest.getUsername(),
                        logRequest.getPassword())
        );
       UserDetails user  = userDetailsService.
               loadUserByUsername(logRequest.getUsername());
       if(user==null){
           throw  new RuntimeException("user not found ");
       }

       String accessToken  = jwtService.generateToken(user.getUsername());
       RefreshToken refreshToken = refreshTokenService.
               generateRefreshToken(logRequest.getUsername());

       TokenResponse tokenResponse = new TokenResponse(
               accessToken,
               refreshToken.getToken());
       return  tokenResponse;
    }

    @Cacheable(key = "#pageable",value = "users")
    public Page<UserResponseDto>getUserById(Pageable pageable) {
        simulateSlowDbCall();
            Page<User> users = userRepository.findAll(pageable);
          return  users.map(
                 user->
                 toResponse(user)
          );
    }

    public TokenResponse createNewToken(RefreshTokenRequest refreshToken) {
        RefreshToken refreshToken2 =
                refreshTokenService.varification(refreshToken.getRefreshToken());
        String username = refreshToken2.getUser().getUsername();
        String newAccessToken = jwtService.generateToken(username);
        RefreshToken newRefreshToken = refreshTokenService
                .refreshTokenRotation(refreshToken2);
        return new TokenResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }

    public String logout(RefreshTokenRequest request) {
        refreshTokenService.revoke(request.getRefreshToken());
        return "Logout successfully";
    }

    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("user not found"));
        userRepository.delete(user);
        return  "user delete successfully";
    }

    public List<UserResponseDto> searchUser(String username, String email) {
        Specification<User> user  = Specification.allOf(
                UserSpecification.hasUsername(username),
                UserSpecification.hasEmail(email)
        );
        List<User> userList= userRepository.findAll(user);
        return userList.stream()
                .map(this::toResponse).toList();
    }

    public UserResponseDto fetchUser(Long id) {
        simulateSlowDbCall();
       User user= userRepository.findById(id)
               .orElseThrow(()->
                       new RuntimeException("User not found"));

        UserResponseDto userResponseDto = toResponse(user);
        return userResponseDto;
    }
    private void simulateSlowDbCall(){
        try {
            Thread.sleep(500);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
