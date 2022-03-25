package com.adilmx.spring_security_jwt_app.service.impl;

import com.adilmx.spring_security_jwt_app.dao.RoleRepo;
import com.adilmx.spring_security_jwt_app.dao.UserRepo;
import com.adilmx.spring_security_jwt_app.dto.UserDto;
import com.adilmx.spring_security_jwt_app.entity.User;
import com.adilmx.spring_security_jwt_app.service.facade.RoleService;
import com.adilmx.spring_security_jwt_app.service.facade.UserService;
import com.adilmx.spring_security_jwt_app.service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepo userRepo;
    @Autowired private RoleService roleService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtil;

    public UserServiceImpl() {
    }

    @Override
    public String logIn(UserDto userDto) {
        User loadedUser = loadUserByUsername(userDto.getUsername());
        String token = jwtUtil.generateToken(loadedUser);
        return token;
    }

    @Override
    public User save(User user) {
        Optional<User> loadedUser = userRepo.findByUsername(user.getUsername());
        if (user != null && !loadedUser.isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            roleService.save(user.getAuthorities());
            return userRepo.save(user);
        }
        return loadedUser.get();
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).get();
        if (user == null || user.getId() == null) {
            throw new UsernameNotFoundException("user " + username + " not found");
        } else {
            return user;
        }
    }
}
