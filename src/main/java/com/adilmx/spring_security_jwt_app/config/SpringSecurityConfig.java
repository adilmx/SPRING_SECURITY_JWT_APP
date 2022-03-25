package com.adilmx.spring_security_jwt_app.config;

import com.adilmx.spring_security_jwt_app.filter.JwtAuthenticationFilter;
import com.adilmx.spring_security_jwt_app.filter.JwtAuthorisationFilter;
import com.adilmx.spring_security_jwt_app.service.facade.UserService;
import com.adilmx.spring_security_jwt_app.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private AuthenticationManager authenticationManager;
    @Autowired JwtAuthorisationFilter jwtAuthorisationFilter;
    @Autowired UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;
    public SpringSecurityConfig() {
    }

    public SpringSecurityConfig( AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //set the userDetailsService and the Encoder for passwords
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    //Authorisation
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() //enable CORS
            .and()
            .csrf().disable() //disable CORS
            .authorizeRequests()
            //AUTHORIZATION FILTERS
            .antMatchers(HttpMethod.POST,"/api/v1/admins/login").permitAll()
            .antMatchers("/api/v1/admins/**").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            //DEFINE A STATELESS POLICY OF HTTP TO DON'T CREATE SESSIONS ID
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Adding an authentication Filter
        http.addFilter(new JwtAuthenticationFilter(authenticationManager));
        //Adding an authorization Filter before the filter UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthorisationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
