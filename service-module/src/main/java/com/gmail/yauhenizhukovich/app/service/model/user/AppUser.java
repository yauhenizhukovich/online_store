package com.gmail.yauhenizhukovich.app.service.model.user;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ROLE_PREFIX;

public class AppUser implements UserDetails {

    private final LoginUserDTO user;
    private final List<SimpleGrantedAuthority> authorities;

    public AppUser(LoginUserDTO user) {
        this.user = user;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
