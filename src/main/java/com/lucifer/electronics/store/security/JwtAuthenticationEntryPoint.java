package com.lucifer.electronics.store.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * \
     * This method will be executed whenever an exception is thrown due to unauthenticated user trying to access the private API's.
     * This method is responsible for handling the initial stage of the authentication process when authentication fails.
     *
     * @param request       - HTTP request that triggered the authentication failure. This object contains information about the request, such as the URL, headers, parameters, and payload.
     * @param response      - HTTP response that will be sent back to the client. This object allows the method to set the response status code, headers, and write content to the response body.
     * @param authException - authentication exception that occurred during the authentication process.
     * @throws IOException      - checked exceptions
     * @throws ServletException - checked exceptions
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//      Setting the HTTP status code of the response to SC_UNAUTHORIZED (401), indicating that the request authentication failed.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      Writing a custom message to the response body indicating that access is denied, along with additional information provided by the AuthenticationException.
        PrintWriter writer = response.getWriter();
        writer.println("Access Denied..!! " + authException.getMessage());
    }
}
