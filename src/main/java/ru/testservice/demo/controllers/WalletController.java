package ru.testservice.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.testservice.demo.dto.WalletDto;
import ru.testservice.demo.exceptions.NoMoneyException;
import ru.testservice.demo.exceptions.NoOperationException;
import ru.testservice.demo.services.WalletService;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletDto> updateWallet(@RequestBody WalletDto request) throws NoMoneyException, NoOperationException {
        if (Objects.isNull(request.getOperationType())) {
            throw new NoOperationException();
        } else {
            return service.updateWallet(request);
        }
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable UUID id) {
        return service.getWallet(id);
    }
}
