package com.lucifer.electronics.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//  Implementing user Authentication : InMemoryMechanism to hardcode User Data.

/*

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

*/

/*
    ------- Implementing user Authentication using Database Approach -----------

    > Two main interfaces

    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;

    1] UserDetails - To represent user details.
    2] UserDetailsService - To get user by username using its method named 'loadUserByUsername'.

    > Instead of InMemory UserDetails(Custom user), we need to provide our own User details present in Database.
    > In order to implement user Authentication using Database approach, we need to provide custom implemention of
      loadUserByUsername method of UserDetailsService interface.
*/

    //  Autowiring CustomUserDetailService
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This method creates and configures a DaoAuthenticationProvider bean. It sets the UserDetailsService and
     * PasswordEncoder for the provider, which are essential components for authenticating users against a data source
     * (such as a database) using Spring Security.
     *
     * @return : The configured DaoAuthenticationProvider instance is returned from the method.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
//      DaoAuthenticationProvider: This is a class provided by Spring Security that implements the AuthenticationProvider interface.
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

//      Setting the UserDetailsService to be used by the DaoAuthenticationProvider during user authentication.
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);

//      Configuring the password encoder(Password encoder bean declared below) for the DaoAuthenticationProvider
//      which will be used for encoding and verifying passwords during user authentication .
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //  This bean is responsible for encoding passwords using the BCrypt hashing algorithm.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    Implementing basic Authentication using Spring Security (Using this we will be able to pass username and
//    password in Postman Authorization section or we need to enter those login credentials inside the browser where
//    alert box asking for the credentials will be generated.

    /**
     * SecurityFilterChain : This method configures the security filter chain, which handles incoming requests and
     *                       applies security rules.
     *
     * @param httpSecurity :  This parameter allows you to configure security settings for HTTP requests.
     *                        It provides methods for customizing various aspects of security, such as CSRF protection,
     *                        CORS configuration, request authorization, etc.
     * @return Configured HttpSecurity object, which represents the configured security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())  // Disabling CSRF (Cross-Site Request Forgery) Protection
                .cors(cors -> cors.disable())  // Disabling CORS (Cross-Origin Resource Sharing) Protection
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
//               configuring HTTP Basic authentication, which prompts the client to provide a username and password for
//               authentication. The Customizer.withDefaults() method applies default settings for HTTP Basic authentication.
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
