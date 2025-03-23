package ru.testservice.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.testservice.demo.dto.WalletDto;

import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.exceptions.NoUuidInMemory;
import ru.testservice.demo.repositories.WalletRepo;
import ru.testservice.demo.staticEntity.Operation;

import java.io.IOException;
import java.util.UUID;

@Service
public class WalletService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final WalletRepo walletRepo;

    public WalletService(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate, WalletRepo walletRepo) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.walletRepo = walletRepo;
    }

    @RabbitListener(queues = "queueRequestGet")
    public void getWallet(String uuid, Message messageRequest) {
        String correlationId = messageRequest.getMessageProperties().getCorrelationId();
        UUID requestUUID;
        try {
            requestUUID = objectMapper.readValue(uuid, UUID.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        WalletDto response = null;
        try {
            response = walletRepo.getWallet(requestUUID);
        } catch (NoUuidInMemory e) {
            throw new RuntimeException(e);
        }

        String responseStr = "";

        try {
            responseStr = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        rabbitTemplate.convertAndSend(
                "queueResponseGet",
                responseStr,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    return message;
                }
        );
    }

    @RabbitListener(queues = "queueRequestUpdate")
    public void updateWallet(String request, Message message) throws NoMoneyException {
        WalletDto postMappingRequest;
        try {
            postMappingRequest = objectMapper.readValue(request, WalletDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (postMappingRequest.getOperationType() == Operation.DEPOSIT) {
            createResponseUpdate(walletRepo.incrementWallet(postMappingRequest), message);
        } else {
            createResponseUpdate(walletRepo.decrementWallet(postMappingRequest), message);
        }

    }

    private void createResponseUpdate(WalletDto wallet, Message messageRequest) {
        String correlationId = messageRequest.getMessageProperties().getCorrelationId();
        String response;
        try {
            response = objectMapper.writeValueAsString(wallet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        rabbitTemplate.convertAndSend("queueResponseUpdate",
                response,
                messageResponse -> {
                    messageResponse.getMessageProperties().setCorrelationId(correlationId);
                    return messageResponse;
                });


    }
}