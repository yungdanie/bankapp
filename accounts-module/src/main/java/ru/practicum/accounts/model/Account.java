package ru.practicum.accounts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String currencyCode;

    private boolean deleted;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    public Account(User user, String currencyCode, BigDecimal balance) {
        this.user = user;
        this.currencyCode = currencyCode;
        this.balance = balance;
    }
}
