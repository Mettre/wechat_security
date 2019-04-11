package com.mettre.security.service;

import com.mettre.security.pojo.JsonPermissions;
import com.mettre.security.pojo.Role;
import com.mettre.security.pojo.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username: %s", username));
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = user.getRoles();
        for (String roleName : roles) {
            Role role = mongoTemplate.findOne(Query.query(Criteria.where("name").is(roleName)), Role.class);
            if (role == null) {
                continue;
            }
            for (JsonPermissions.SimplePermission permission : role.getPermissions()) {
                for (String privilege : permission.getPrivileges().keySet()) {
                    authorities.add(new SimpleGrantedAuthority(String.format("%s-%s", permission.getResourceId(), privilege)));
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
    }
}