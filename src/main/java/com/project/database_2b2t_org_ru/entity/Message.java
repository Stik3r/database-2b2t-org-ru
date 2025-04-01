package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "message_dbt")
public class Message {

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
    private List<byte[]> thumbnails;

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

    public List<byte[]> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<byte[]> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
