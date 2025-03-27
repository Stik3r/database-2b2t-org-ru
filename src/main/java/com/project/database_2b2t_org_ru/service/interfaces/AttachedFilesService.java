package com.project.database_2b2t_org_ru.service.interfaces;

import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachedFilesService extends MainService<AttachedFiles> {
    List<AttachedFiles> processFiles(MultipartFile[] files, long messageId) throws IOException;
    List<AttachedFiles> findAllByMessageId(long messageId);
} 