package com.lucifer.electronics.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

//  Manually configuring users using UserDetailsService bean. (Spring security will use this bean in order to configure user)
    @Bean
    public UserDetailsService userDetailsService() {

//      Creating User (Defining user details using User objects or UserBuilder provided by Spring Security.)
        UserDetails adminUser = User.builder()
                .username("Aaron")
//               Encoding the password using password encoder bean declared below.
                .password(passwordEncoder().encode("finch"))
                .roles("ADMIN")
                .build();

//      Creating User-2
        UserDetails normalUser = User.builder()
                .username("Michael")
                .password(passwordEncoder().encode("pass"))
                .roles("NORMAL")
                .build();

//      InMemoryUserDetailsManager is an implementation class of UserDetailsService
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(adminUser, normalUser);
        return userDetailsManager;
    }

//  This bean is responsible for encoding passwords using the BCrypt hashing algorithm.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
