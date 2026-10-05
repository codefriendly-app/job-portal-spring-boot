package job_portat.demo.Service;

import job_portat.demo.Entity.User;
import job_portat.demo.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    UserRepository userRepository;
    UserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("User not found")
        );
//        if(user==null){
//            throw  new UsernameNotFoundException("User not Found");
//        }
        return new CustomUserDetailsService(user);
    }
}
