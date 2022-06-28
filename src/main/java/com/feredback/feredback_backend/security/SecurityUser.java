package com.feredback.feredback_backend.security;
import com.feredback.feredback_backend.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-11 14:46
 **/
@Data
public class SecurityUser implements UserDetails {
    /**
     * current logged-in user
     */
    private transient User loggedInUser;

    public SecurityUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (loggedInUser.getIsAdmin() == 1) {
            SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("admin");
            authorities.add(adminAuthority);
        }
        if (loggedInUser.getIsSubjectCoordinator() == 1) {
            SimpleGrantedAuthority coordinatorAuthority = new SimpleGrantedAuthority("coordinator");
            authorities.add(coordinatorAuthority);
        }
        if (loggedInUser.getIsHeadTutor() == 1) {
            SimpleGrantedAuthority coordinatorAuthority = new SimpleGrantedAuthority("headTutor");
            authorities.add(coordinatorAuthority);
        }

        authorities.add(new SimpleGrantedAuthority("tutor"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return loggedInUser.getPassword();
    }

    @Override
    public String getUsername() {
        return loggedInUser.getEmail();
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
        return (loggedInUser.getIsDeleted() == 0);
    }
}
