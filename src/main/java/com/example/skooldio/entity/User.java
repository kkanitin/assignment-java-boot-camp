package com.example.skooldio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(
            generator = "users_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "users_seq", allocationSize = 1, sequenceName = "users_seq")
    @Column(name="ID")
    private long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
