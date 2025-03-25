package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thread_dbt")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "head_thread")
    private String head_thread;

    @Column(name = "body")
    private String body;

    @Column(name = "date_Time")
    private LocalDateTime dateTime;

    @Column(name = "first_message_id")
    private int firstMessageID;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead_thread() {
        return head_thread;
    }

    public void setHead_thread(String head_thread) {
        this.head_thread = head_thread;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getFirstMessageID() {
        return firstMessageID;
    }

    public void setFirstMessageID(int firstMessageID) {
        this.firstMessageID = firstMessageID;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
