package com.shortlink.webapp.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.shortlink.webapp.entity.enums.Permission.*;

//@RequiredArgsConstructor
@AllArgsConstructor
public enum Role {//implements GrantedAuthority {


    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    private static final String ROLE_PREFIX = "ROLE_";

    public List<SimpleGrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions());
//        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + name()));
//        return authorities;
        List<SimpleGrantedAuthority> authority = permissions
                .stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authority.add(new SimpleGrantedAuthority(ROLE_PREFIX + name()));
        return authority;

    }


//    public List<SimpleGrantedAuthority> getAuthorities() {
//        var authorities = getPermissions()
//                .stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toList());
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return authorities;
//    }
}
