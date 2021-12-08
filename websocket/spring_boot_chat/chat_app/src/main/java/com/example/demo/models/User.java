package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users_tbl")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Room> rooms = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
