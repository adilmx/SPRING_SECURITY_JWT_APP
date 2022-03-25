package com.adilmx.spring_security_jwt_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_APP")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked= true;
    private boolean credentialsNonExpired= true;
    private boolean enabled= true;

    public User( String username,String password) {
        this.password = password;
        this.username = username;
    }
}
