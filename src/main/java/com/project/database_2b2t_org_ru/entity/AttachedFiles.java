package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "att_files_dbt")
public class AttachedFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_bytea")
    @Basic(fetch = FetchType.LAZY)
    private byte[] fileBytea;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(name = "thumbnail_base64image")
    private String thumbnailbase64image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileBytea() {
        return fileBytea;
    }

    public void setFileBytea(byte[] fileBytea) {
        this.fileBytea = fileBytea;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getThumbnailbase64image() {
        return thumbnailbase64image;
    }

    public void setThumbnailbase64image(String thumbnailbase64image) {
        this.thumbnailbase64image = thumbnailbase64image;
    }
}
