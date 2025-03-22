package ru.testservice.demo.models;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Entity
@Table(name ="wallet")
public class Wallet {
    @Id
    @Column(name="wallet_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount")
    private Long amount;

    public UUID getId() {
        return id;
    }
    public void setId(UUID uuid) {
        this.id = uuid;
    }
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
