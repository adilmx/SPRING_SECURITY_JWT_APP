package com.adilmx.spring_security_jwt_app.filter;

import com.adilmx.spring_security_jwt_app.constant.JwtConstant;
import com.adilmx.spring_security_jwt_app.service.facade.UserService;
import com.adilmx.spring_security_jwt_app.service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorisationFilter extends OncePerRequestFilter {
    @Autowired private UserService userService;
    @Autowired private JwtUtils jwtUtil;



    public JwtAuthorisationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get the Bearer+Token from the header of request
        String authorization = request.getHeader(JwtConstant.AUTHORIZATION);
        String token = null;
        String usernameFromToken = null;

        //verify if Bearer Exists and the token if it is not null
        if (authorization != null && authorization.startsWith(JwtConstant.BEARER)) {
            //get the token by removing the bearer
            token = authorization.substring(JwtConstant.BEARER.length());
            //get the username from the token
            System.out.println(token);
            usernameFromToken = jwtUtil.getUsernameFromToken(token);
        }
        //if the context of the application has no auth Principal with the same userDetails
        if (usernameFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //get the userDetails from the DB
            UserDetails userDetails = userService.loadUserByUsername(usernameFromToken);
            //verify the validation of the token with this Principal
            if (jwtUtil.validateToken(token, userDetails)) {
                //if token is valid then register the Principal to the Context of the Application authentication
                jwtUtil.registerAuthenticationTokenInContext(userDetails, request);
            }
        }
        //move to the next filter(mostly UsernamePasswordAuthenticationFilter)
        filterChain.doFilter(request,response);
    }
}
