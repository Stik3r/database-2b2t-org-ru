package com.project.database_2b2t_org_ru.controller;

import com.project.database_2b2t_org_ru.entity.AttachedFiles;
import com.project.database_2b2t_org_ru.service.interfaces.AttachedFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private AttachedFilesService attachedFilesService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        AttachedFiles file = attachedFilesService.getObjectById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getFileType()))
                .body(file.getFileBytea());
    }
} 