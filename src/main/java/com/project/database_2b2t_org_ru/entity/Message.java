package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

@Entity
@Table(name="message_dbt")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "message_body")
    private String message_body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
