package com.project.database_2b2t_org_ru.dao;

import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachedFilesRepository extends JpaRepository<AttachedFiles, Long> {
    List<AttachedFiles> findByMessageId(long messageId);
} 