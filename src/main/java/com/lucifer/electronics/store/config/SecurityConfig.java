package com.lucifer.electronics.store.config;

import com.lucifer.electronics.store.security.JwtAuthenticationEntryPoint;
import com.lucifer.electronics.store.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
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

    /*

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

    */

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String [] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/login")
                        .permitAll())
//              making API "/user/create" public & allowing it to be accessed by anyone without requiring authentication.
                .authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.POST, "/user/create")
                        .permitAll())
//              making API "/auth/google" public & allowing it to be accessed by anyone without requiring authentication.
                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/google")
                        .permitAll())
//              making URLs related to swagger public & allowing it to be accessed by anyone without requiring authentication.
                .authorizeHttpRequests(requests -> requests.requestMatchers(PUBLIC_URLS)
                        .permitAll())
//              configuring API "/user/delete/**" so that, users can only be deleted by the User who has Admin Role
                .authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasRole("ADMIN"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//  Way - 1 : CORS Configuration

    /**
     * configuring a CORS (Cross-Origin Resource Sharing) filter using Spring Boot. This method creates and configures a FilterRegistrationBean for the CORS filter.
     * CORS is a mechanism that allows resources on a web page to be requested from another domain outside the domain from which the resource originated. (CORS is a mechanism that indicates to the application to load up the resources or not when the request is coming from different origin.)
     * This configuration sets up a CORS filter that allows cross-origin requests from specified origins, headers, and methods, and registers it with the Spring application context.
     *
     * @return  the configured FilterRegistrationBean instance, which will be registered as a bean in the Spring application context.
     */

    @Bean
    public FilterRegistrationBean processCorsFilter(){
//      UrlBasedCorsConfigurationSource class defines a source of CORS configuration based on URL patterns.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

//      CorsConfiguration class represents the CORS configuration for a specific set of origins, HTTP methods, headers, etc.
        CorsConfiguration config = new CorsConfiguration();

//      Specifying whether the browser should include any cookies associated with the request origin in the request.
        config.setAllowCredentials(true);
//        Specifying the allowed origins (domains) from which requests can be made.
//        config.setAllowedOrigins(Arrays.asList("https://domain1.com", "https://domain2.com"));

//      Specifying the allowed origin from which requests can be made
        config.addAllowedOrigin("http://localhost:4200");

//      Adding allowed headers that can be included in the request.
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
//        config.addAllowedHeader("*");

//      Adding allowed HTTP methods for requests.
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

//      Specifying how long (in seconds) the results of a preflight request (OPTIONS) can be cached.
        config.setMaxAge(3600L);

//      Registering the CORS configuration to apply to all URL paths.
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

//      Setting the order of the filter in the filter chain. Negative values indicate that the filter should be applied before the default filters.
//      Setting the order of the current bean so that it gets preference in order to get registered early.
        bean.setOrder(-100);
        return bean;
    }
}
