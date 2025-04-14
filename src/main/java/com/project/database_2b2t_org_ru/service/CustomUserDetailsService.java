package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.RoleRepository;
import com.project.database_2b2t_org_ru.dao.UserRepository;
import com.project.database_2b2t_org_ru.entity.Role;
import com.project.database_2b2t_org_ru.entity.User;
import com.project.database_2b2t_org_ru.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("Пользователь с email " + email + " не найден");
        }

        return new CustomUserDetails(user);
    }

    public void registerNewUser(User newUser) {

        User user = userRepository.findByEmail(newUser.getEmail());
        if (user != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        var roles = new HashSet<Role>();
        roles.add(roleRepository.getById(Role.ROLES.ROLE_USER.getId()));

        newUser.setRoles(roles);
        newUser.setLocked(false);
        userRepository.save(newUser);

        return ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
