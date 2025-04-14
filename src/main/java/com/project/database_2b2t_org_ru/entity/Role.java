package com.project.database_2b2t_org_ru.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role_dbt")
public class Role {

    public enum ROLES {
        ROLE_EMPTY((short) 1),
        ROLE_ADMIN((short) 2),
        ROLE_MODER((short) 3),
        ROLE_USER((short) 4),
        ROLE_GUEST((short) 5);

        private final short id;

        ROLES(short id){
            this.id = id;
        }

        public short getId(){
            return id;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private short id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
