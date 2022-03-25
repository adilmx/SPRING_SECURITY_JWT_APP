package com.adilmx.spring_security_jwt_app.service.facade;

import com.adilmx.spring_security_jwt_app.dto.UserDto;
import com.adilmx.spring_security_jwt_app.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

//extends from the Interface UserDetailsService
public interface UserService extends UserDetailsService {
    String logIn(UserDto userDto);
    User save(User user);
    List<User> findAll();
}
