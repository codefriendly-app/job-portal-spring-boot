package job_portat.demo.Service;

import job_portat.demo.Entity.RefreshToken;
import job_portat.demo.Entity.User;
import job_portat.demo.Repository.RefreshTokenRepository;
import job_portat.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
@Service
public class RefreshTokenService {

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository,
                               RefreshTokenRepository refreshTokenRepository){
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;

    }

    public RefreshToken generateRefreshToken(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("user not found")
        );
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(
                UUID.randomUUID().toString()
        );
        refreshToken.setExpire(new Date(System.currentTimeMillis()+1000l*60*60*24*7));
        refreshToken.setRevocation(false);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken varification(String refreshToken){
        RefreshToken refreshToken1 = refreshTokenRepository
                .findByToken(refreshToken).orElseThrow(()->
                        new RuntimeException("Token not found"));
        if(refreshToken1.isRevocation()){
            throw new RuntimeException("Refresh Token is Revoke");
        }
        if(refreshToken1.getExpire().before(new Date())){
            throw  new RuntimeException("Token was expired ");
        }
        return refreshToken1;
    }

    public void revoke(String token){
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token).orElseThrow(()->
                        new RuntimeException("Token not found"));
        refreshToken.setRevocation(true);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken  refreshTokenRotation(RefreshToken oldToken){
         oldToken.setRevocation(true);
        refreshTokenRepository.save(oldToken);
         String username = oldToken.getUser().getUsername();
        return generateRefreshToken(username);

    }
}
