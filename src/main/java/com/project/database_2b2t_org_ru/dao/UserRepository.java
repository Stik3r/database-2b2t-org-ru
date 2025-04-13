package com.project.database_2b2t_org_ru.dao;

import com.project.database_2b2t_org_ru.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
