package com.project.database_2b2t_org_ru.dao;

import com.project.database_2b2t_org_ru.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Integer> {
}
