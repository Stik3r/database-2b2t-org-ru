package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "message_dbt")
public class Message {

    public static class Thumbnail{
        private long imageId;
        private String base64Image;
        private String sourceType;
        public long getImageId() {
            return imageId;
        }

        public void setImageId(long imageId) {
            this.imageId = imageId;
        }

        public String getBase64Image() {
            return base64Image;
        }

        public void setBase64Image(String base64Image) {
            this.base64Image = base64Image;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "message_body")
    private String message_body;

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private Thread thread;

    @Column(name = "date_Time")
    private LocalDateTime dateTime;

    @Transient
    private List<AttachedFiles> attachedFiles;

    @Transient
    private List<Thumbnail> thumbnails;

    public List<AttachedFiles> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<AttachedFiles> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
