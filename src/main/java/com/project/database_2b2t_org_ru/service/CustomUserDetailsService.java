package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.UserRepository;
import com.project.database_2b2t_org_ru.entity.User;
import com.project.database_2b2t_org_ru.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("Пользователь с email " + email + " не найден");
        }

        return new CustomUserDetails(user);
    }
}
