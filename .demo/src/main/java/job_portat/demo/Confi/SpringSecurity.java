package job_portat.demo.Confi;

import job_portat.demo.Service.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

  private JwtFilter jwtFilter;
  private AuthenticationEntrypoint authenticationEntrypoint;
  private AuthorizationHandler authorizationHandler;
  public SpringSecurity(JwtFilter jwtFilter,
                        AuthenticationEntrypoint authenticationEntrypoint,
                        AuthorizationHandler authorizationHandler){
      this.jwtFilter = jwtFilter;
      this.authenticationEntrypoint = authenticationEntrypoint;
      this.authorizationHandler = authorizationHandler;
  }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((request) ->
                        request.requestMatchers("/user/**").permitAll()
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(ex->
                        ex.authenticationEntryPoint(authenticationEntrypoint)
                                .accessDeniedHandler(authorizationHandler))
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)throws Exception{
          return  configuration.getAuthenticationManager();
    }
}
