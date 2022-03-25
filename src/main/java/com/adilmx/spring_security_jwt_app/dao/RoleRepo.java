package com.adilmx.spring_security_jwt_app.dao;

import com.adilmx.spring_security_jwt_app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findByAuthority(String authority);
}
