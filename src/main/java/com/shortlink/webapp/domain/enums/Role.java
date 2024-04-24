package com.shortlink.webapp.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@AllArgsConstructor
public enum Role {//implements GrantedAuthority {


    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_CREATE,
                    Permission.MANAGER_READ,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    Permission.MANAGER_READ,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.MANAGER_CREATE
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
