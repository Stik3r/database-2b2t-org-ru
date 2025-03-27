package com.project.database_2b2t_org_ru.service;

import com.project.database_2b2t_org_ru.dao.AttachedFilesRepository;
import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import com.project.database_2b2t_org_ru.entity.Message;
import com.project.database_2b2t_org_ru.service.interfaces.AttachedFilesService;
import com.project.database_2b2t_org_ru.service.interfaces.MessageService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttachedFilesServiceImpl implements AttachedFilesService {
    
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private final Tika tika = new Tika();

    @Autowired
    private AttachedFilesRepository attachedFilesRepository;
    
    @Autowired
    private MessageService messageService;

    @Override
    public List<AttachedFiles> getAllObjects() {
        return attachedFilesRepository.findAll();
    }

    @Override
    public void saveObject(AttachedFiles object) {
        attachedFilesRepository.save(object);
    }

    @Override
    public AttachedFiles getObjectById(long id) {
        return attachedFilesRepository.findById((long) id).orElse(null);
    }

    @Override
    public void deleteObjectById(long id) {
        attachedFilesRepository.deleteById(id);
    }

    @Override
    public List<AttachedFiles> processFiles(MultipartFile[] files, long messageId) throws IOException {
        List<AttachedFiles> attachedFiles = new ArrayList<>();
        Message message = messageService.getObjectById(messageId);
        
        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            
            // Проверка размера файла
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File " + file.getOriginalFilename() + " exceeds maximum size of 50MB");
            }
            
            // Определение реального типа файла через Tika
            String detectedType = tika.detect(file.getBytes());
            
            // Проверка типа файла
            if (!isAllowedFileType(detectedType)) {
                throw new IllegalArgumentException("File type " + detectedType + " is not allowed");
            }
            
            // Создание и настройка AttachedFiles
            AttachedFiles attachedFile = new AttachedFiles();
            attachedFile.setFileName(file.getOriginalFilename());
            attachedFile.setFileType(detectedType);
            attachedFile.setFileBytea(file.getBytes());
            attachedFile.setMessage(message);
            
            attachedFiles.add(attachedFile);
        }
        
        return attachedFilesRepository.saveAll(attachedFiles);
    }
    
    @Override
    public List<AttachedFiles> findAllByMessageId(long messageId) {
        return attachedFilesRepository.findByMessageId(messageId);
    }
    
    private boolean isAllowedFileType(String contentType) {
        return contentType != null && 
               (contentType.startsWith("image/") || contentType.startsWith("video/"));
    }
} 