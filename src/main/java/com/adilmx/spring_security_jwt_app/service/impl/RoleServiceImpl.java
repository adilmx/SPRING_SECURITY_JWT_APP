package com.adilmx.spring_security_jwt_app.service.impl;

import com.adilmx.spring_security_jwt_app.dao.RoleRepo;
import com.adilmx.spring_security_jwt_app.entity.Role;
import com.adilmx.spring_security_jwt_app.service.facade.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role save(Role role) {
        if(role != null){
            Role loadedRole = findByAuthority(role.getAuthority());
            if(loadedRole == null) {
                return roleRepo.save(role);
            }else{
                return loadedRole;
            }
        }
        return null;
    }

    @Override
    public Collection<Role> save(Collection<Role> roles) {
        Role loadedRole = null;
        Collection<Role> loadedRoles = new ArrayList<>();
        if(!roles.isEmpty()){
            for(Role role : roles){
                loadedRole = findByAuthority(role.getAuthority());
                if(loadedRole == null){
                    loadedRoles.add(role);
                    roleRepo.save(role);
                }else{
                    loadedRoles.add(loadedRole);
                }
            }
            return loadedRoles;
        }
        return null;
    }

    @Override
    public Role findByAuthority(String authority) {
        Optional<Role> role = roleRepo.findByAuthority(authority);
        if(role.isPresent()) return roleRepo.findByAuthority(authority).get();
        return null;
    }
}
