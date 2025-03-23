package ru.testservice.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.testservice.demo.dto.WalletDto;
import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.exceptions.NoOperationException;
import ru.testservice.demo.services.WalletService;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public WalletController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletDto> updateWallet(@RequestBody WalletDto request) throws NoMoneyException, NoOperationException {
        if (Objects.isNull(request.getOperationType())) {
            throw new NoOperationException();
        } else if (request.getAmount() == 0) {
            throw new NoMoneyException();
        } else {
            return handlerUpdate(request);
        }
    }

        @GetMapping("/wallets/{id}")
        public ResponseEntity<WalletDto> getWallet (@PathVariable UUID id){
            return handlerGet(id);
        }

        private ResponseEntity<WalletDto> handlerGet(UUID id){
            String correlationId = UUID.randomUUID().toString();
            String request = "";

            try {
                request = objectMapper.writeValueAsString(id);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            rabbitTemplate.convertAndSend("exchange-get", "request.get", request , message -> {
                message.getMessageProperties().setCorrelationId(correlationId);
                return message;});

            try {
                String response = (String) rabbitTemplate.receiveAndConvert("queueResponseGet");

                WalletDto responseMod;

                responseMod = objectMapper.readValue(response, WalletDto.class);

                return ResponseEntity.ok(responseMod);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        private ResponseEntity<WalletDto> handlerUpdate (WalletDto request){
            String postMappingRequest = "";
            try {
                postMappingRequest = objectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            rabbitTemplate.convertAndSend("exchange-update", "request.update", postMappingRequest, messag -> {
                messag.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
                return messag;
            });
            try {
                String response = (String) rabbitTemplate.receiveAndConvert("queueResponseUpdate");

                WalletDto responsePostService;

                responsePostService = objectMapper.readValue(response, WalletDto.class);

                return ResponseEntity.status(HttpStatus.OK).body(responsePostService);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
