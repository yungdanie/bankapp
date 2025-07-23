package ru.practicum.accounts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false, unique = true)
    public String login;

    @Column(nullable = false)
    public LocalDate birthdate;

    public User(String name, String password, String login, LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.birthdate = birthdate;
    }
}
