package com.adilmx.spring_security_jwt_app.service.facade;

import com.adilmx.spring_security_jwt_app.entity.Role;

import java.util.Collection;

public interface RoleService {
    Role save(Role role);
    Collection<Role> save(Collection<Role> roles);
    Role findByAuthority(String authority);
}
