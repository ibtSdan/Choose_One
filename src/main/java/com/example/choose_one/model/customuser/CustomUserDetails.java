package com.example.choose_one.model.customuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Long userId;

    private final String username;

    private final String encodePw;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String username, String encodePw, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.encodePw = encodePw;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return encodePw;
    }
}
