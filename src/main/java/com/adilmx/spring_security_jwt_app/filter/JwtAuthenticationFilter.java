package com.adilmx.spring_security_jwt_app.filter;

import com.adilmx.spring_security_jwt_app.config.SpringSecurityConfig;
import com.adilmx.spring_security_jwt_app.constant.JwtConstant;
import com.adilmx.spring_security_jwt_app.dto.UserDto;
import com.adilmx.spring_security_jwt_app.entity.User;
import com.adilmx.spring_security_jwt_app.service.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationFilter() {
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDto userDto = null;
        try {
            //get the user from the request
            userDto= new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            //authenticate based on the SpringSecurityConfig that we made
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("bad credentials!");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //if the authentication was successfully done
        //get the Principal(User)
        User principal = (User) authResult.getPrincipal();
        //Generate the token
        String token = new JwtUtils().generateToken(principal);
        //add to the header of the response ["Authorisation":"Bearer Token"]
        response.addHeader(JwtConstant.AUTHORIZATION,JwtConstant.BEARER+token);
    }
}
