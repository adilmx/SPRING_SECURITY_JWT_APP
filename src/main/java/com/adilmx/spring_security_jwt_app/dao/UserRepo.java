package com.adilmx.spring_security_jwt_app.dao;

import com.adilmx.spring_security_jwt_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
