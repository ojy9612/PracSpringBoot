package com.homework.hanghae99homework02.security;

import com.homework.hanghae99homework02.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String email;

    private final String nickName;

    private final List<String> roles;

    private String password;

    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.nickName = user.getNickname();
    }

    /**
     * password 안쓰여서 생성자 안 만듬
     */

    public String getNickName() {
        return nickName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}