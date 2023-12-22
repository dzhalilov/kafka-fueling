package org.example.apifueling.controller;

import lombok.RequiredArgsConstructor;
import org.example.apifueling.domain.FuelingOrderStatus;
import org.example.apifueling.dto.FuelingOrderDto;
import org.example.apifueling.dto.OrderStatusDto;
import org.example.apifueling.service.CashedStatusService;
import org.example.apifueling.service.FuelingService;
import org.example.apifueling.service.OrderStatusService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fueling")
@RequiredArgsConstructor
public class FuelingController {

    private final FuelingService fuelingService;
    private final OrderStatusService orderStatusService;
    private final CashedStatusService cashedStatusService;

    @PostMapping(value = "/order", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<OrderStatusDto> order(@RequestBody FuelingOrderDto dto) {
        OrderStatusDto order = fuelingService.order(dto);
        return Flux.just(order).concatWith(cashedStatusService.getOrderStatusByIdV2(order));
    }

    // for demonstration purposes in browser
    @GetMapping(value = "/random", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<OrderStatusDto> random() {
        UUID id = UUID.randomUUID();
        OrderStatusDto dto = new OrderStatusDto(id, FuelingOrderStatus.CREATED);
        return getFixedStatuses(dto);
    }

    @GetMapping("/status/{orderId}")
    public OrderStatusDto getStatus(@PathVariable UUID orderId) {
        return orderStatusService.getStatus(orderId);
    }

    private Flux<OrderStatusDto> getFixedStatuses(OrderStatusDto dto) {
        FuelingOrderStatus[] statuses = FuelingOrderStatus.values();
        return Flux.generate(() -> 0, (state, sink) -> {
            FuelingOrderStatus status = statuses[state];
            sink.next(new OrderStatusDto(dto.getId(), status));
            if (status.equals(FuelingOrderStatus.COMPLETED) || status.equals(FuelingOrderStatus.CANCELED)) {
                sink.complete();
            }
            return state + 1;
        }).delayElements(Duration.ofSeconds(1))
                .cast(OrderStatusDto.class)
                .log();
    }
}
