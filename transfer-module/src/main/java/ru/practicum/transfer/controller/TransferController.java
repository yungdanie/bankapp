package ru.practicum.transfer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.common.dto.Transfer;
import ru.practicum.transfer.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public Mono<Void> transfer(@RequestBody Transfer transfer) {
        return transferService.transfer(transfer);
    }
}
