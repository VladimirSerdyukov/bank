package ru.testservice.demo.repositories;

import ru.testservice.demo.dto.WalletDto;
import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.exceptions.NoUuidInMemory;

import java.util.UUID;

public interface WalletRepo {
    WalletDto incrementWallet(WalletDto wallet);
    WalletDto decrementWallet(WalletDto wallet) throws NoMoneyException;
    WalletDto getWallet(UUID uuid) throws NoUuidInMemory;
}
