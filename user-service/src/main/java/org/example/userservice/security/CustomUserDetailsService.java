package org.example.userservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.context.annotation.Lazy;


@Service
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserService userService;

    public CustomUserDetailsService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userService.findBUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with that username:" + username));

        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword()); // Return a UserPrincipal object with user details
    }
}