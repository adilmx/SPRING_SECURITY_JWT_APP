package com.adilmx.spring_security_jwt_app;

import com.adilmx.spring_security_jwt_app.entity.Role;
import com.adilmx.spring_security_jwt_app.entity.User;
import com.adilmx.spring_security_jwt_app.service.facade.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringSecurityJwtAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            User admin = new User("admax", "admax");
            admin.setAuthorities(Arrays.asList(new Role("ROLE_ADMIN")));
            userService.save(admin);
            userService.findAll().forEach(user -> {
                System.out.println(user.getUsername());
            });
        };
    }

}
