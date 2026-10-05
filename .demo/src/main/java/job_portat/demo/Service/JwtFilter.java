package job_portat.demo.Service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService,UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
         String authHeader = request.getHeader("Authorization");
         if(authHeader==null||!authHeader.startsWith("Bearer ")){
             filterChain.doFilter(request ,response);
             return;
         }
         String token  = authHeader.substring(7);
         try {


             String username = jwtService.extractUser(token);
             UserDetails userDetails = userDetailsService
                     .loadUserByUsername(username);
             UsernamePasswordAuthenticationToken authenticationToken =
                     new UsernamePasswordAuthenticationToken(
                             userDetails,
                             null,
                             userDetails.getAuthorities()
                     );
             SecurityContextHolder.getContext()
                     .setAuthentication(authenticationToken);
         }catch (Exception e){
             SecurityContextHolder.clearContext();
         }
        filterChain.doFilter(request,response);
    }
}
