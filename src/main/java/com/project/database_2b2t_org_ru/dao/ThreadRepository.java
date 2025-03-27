package com.project.database_2b2t_org_ru.dao;

import com.project.database_2b2t_org_ru.entity.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {

    Page<Thread> findAll(Pageable pageable);
}
