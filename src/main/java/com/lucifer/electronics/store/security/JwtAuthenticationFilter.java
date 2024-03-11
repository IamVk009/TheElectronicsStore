package com.lucifer.electronics.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * For any incoming request, this Filter class gets executed. It checks if the request has valid JWT token.
 * If it has valid JWT token, then it sets the Authentication in the security context, to specify that the current user is authenticated.
 * It Performs following tasks -
 *  1. Get JWT token from the request
 *  2. Validate the token.
 *  3. Get username from the token.
 *  4. Load user associated with the token.
 *  5. Set Authentication.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This method is responsible for processing incoming HTTP requests and responses and optionally modifying them or performing additional processing.
     * @param request - incoming HTTP request from the client.
     * @param response - HTTP response that will be sent back to the client.
     * @param filterChain - The chain of filters configured in the Spring application. The doFilterInternal method typically delegates further processing of the request to the next filter in the chain by calling filterChain.doFilter(request, response).
     * @throws ServletException - Checked exception
     * @throws IOException - Checked exception
     *
     * Overall, the doFilterInternal method serves as the entry point for custom filter logic and provides a way to intercept and manipulate incoming HTTP requests and responses in a Spring application.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        log.info("Header : {}", requestHeader);

//      Token example - Bearer bWl0aGFsaUBleGFtcGxlLmNvbTp0cmFpbGJsYXplcg==
        String token = null;
        String username = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
//          Extracting token value from request header.
            token = requestHeader.substring(7);
            log.info("Token - {}", token);
//          Extracting username from token
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
                log.info("Username - {}", username);
            } catch (IllegalArgumentException e) {
                log.error("Illegal Argument while fetching username from tokem....");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                log.error("Provided Jwt token is expired");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                log.error("Some changes has been done in token ...! Invalid Token ");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("Invalid Header Value !!");
        }

//      If username is not null (username != null) and nobody is logged in (SecurityContextHolder.getContext().getAuthentication() == null)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//          Fetching user details from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//          Validating JWT token
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
//          If token is valid, then set authentication
            if (validateToken) {
//              Creating Authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Set Authentication successful {}", authentication);
//              Setting Authentication to security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info("Token Validation Failed...!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
