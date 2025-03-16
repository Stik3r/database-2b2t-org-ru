package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="message_dbt")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "message_body")
    private String message_body;

    @Column(name = "thread_id")
    private int threadId;

    @Column(name = "date_Time")
    private LocalDateTime dateTime;

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
