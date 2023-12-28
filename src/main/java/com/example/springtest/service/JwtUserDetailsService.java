package com.example.springtest.service;

import com.example.springtest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        List<SimpleGrantedAuthority> authoritiesList;

        if (user.getUsername().equals(username)) {
            authoritiesList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_AUTH"));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getUPassword(),
                    authoritiesList);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}