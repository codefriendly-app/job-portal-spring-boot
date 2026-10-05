package job_portat.demo.Service;

import job_portat.demo.Entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetailsService implements UserDetails{
       private User user;
       CustomUserDetailsService(User user){
           this.user = user;
       }
    @Override
    @Transactional(readOnly = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
           List<GrantedAuthority> authorities = new ArrayList<>();
           String rolename = "USER";

           // role
           if(user.getRole()!=null) {
               authorities.add(
                       new SimpleGrantedAuthority(
                               "ROLE_"+user.getRole().getName()
                       )
               );
                rolename= user.getRole().getName();
           }

           //permission

        if(user.getRole().getPermission()!=null){
            user.getRole()
                    .getPermission()
                    .forEach(permission->
                            authorities.add(
                                    new SimpleGrantedAuthority(
                                            permission.getName()
                                    )
                            ));
        }
        return authorities;

    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}