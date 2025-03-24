package ru.testservice.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.testservice.demo.models.Wallet;
import ru.testservice.demo.staticEntity.Operation;

import java.util.Objects;
import java.util.UUID;

@Component
@Scope(value = "prototype")
public class WalletDto {
    private UUID walletId;
    private Operation operationType;
    private Long amount;

    public WalletDto() {}

    public WalletDto(Wallet wallet){
        this.amount = wallet.getAmount();
        this.walletId = wallet.getId();
    }

    public WalletDto(UUID walletId, Operation operationType, Long amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @JsonIgnore
    public Wallet getWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(this.walletId);
        wallet.setAmount(this.getAmount());
        wallet.setId(this.getWalletId());
        return wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletDto walletDto = (WalletDto) o;
        return walletId.toString().equals(walletDto.walletId.toString())
                    && operationType == walletDto.operationType
                && amount.longValue() == walletDto.amount.longValue();
    }

    @Override
    public int hashCode() {
        return (int) (31 * ((walletId == null ? 0 : walletId.hashCode()) +
                (amount == null ? 0 : amount)));
    }

    @Override
    public String toString() {
        return "WalletDto{" +
                "walletId=" + walletId +
                ", operationType=" + operationType +
                ", amount=" + amount +
                '}';
    }
}
