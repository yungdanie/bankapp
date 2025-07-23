package ru.practicum.exchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "exchange_rate",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_currency_code", "to_currency_code"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    public ExchangeRate(String fromCurrencyCode, String toCurrencyCode, BigDecimal rate) {
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.rate = rate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fromCurrencyCode;

    @Column(nullable = false)
    private String toCurrencyCode;

    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal rate;
}
