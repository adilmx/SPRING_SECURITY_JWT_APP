package com.adilmx.spring_security_jwt_app.controller;

import com.adilmx.spring_security_jwt_app.dto.UserDto;
import com.adilmx.spring_security_jwt_app.entity.User;
import com.adilmx.spring_security_jwt_app.service.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    private UserService userService;
    @Autowired AuthenticationManager authenticationManager;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public String logIn(@RequestBody UserDto userDto) {
        try {
            if (userDto != null) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            }else{
                return "The User Data are empty!";
            }
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("BAD CREDENTIALS");
        }
        return userService.logIn(userDto);
    }


    @GetMapping("/")
    public List<User> findAll() {
        return userService.findAll();
    }


    @GetMapping("/username/{username}")
    public UserDetails loadUserByUsername(@PathVariable String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }
}
