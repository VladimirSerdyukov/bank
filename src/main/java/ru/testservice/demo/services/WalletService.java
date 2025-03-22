package ru.testservice.demo.services;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.testservice.demo.dto.WalletDto;

import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.repositories.WalletRepo;
import ru.testservice.demo.staticEntity.Operation;

import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    WalletRepo walletRepo;

    public ResponseEntity<WalletDto> getWallet(UUID uuid) {
        WalletDto wallet = walletRepo.getWallet(uuid);

        ResponseEntity<WalletDto> response;
        if (wallet != null) {
            response = ResponseEntity.status(HttpStatus.OK).body(wallet);
        } else {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new WalletDto());
        }
        return response;
    }

    public ResponseEntity<WalletDto> updateWallet(WalletDto request) throws NoMoneyException {
        ResponseEntity<WalletDto> response = null;

        if (request.getAmount() == 0L) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(request);
        } else {
            if (request.getOperationType() == Operation.DEPOSIT) {
                response = ResponseEntity.status(HttpStatus.OK).body(walletRepo.incrementWallet(request));
            } else {
                WalletDto wallet = walletRepo.decrementWallet(request);
                response = ResponseEntity.status(HttpStatus.OK).body(wallet);
            }
        }
        return response;
    }
}
