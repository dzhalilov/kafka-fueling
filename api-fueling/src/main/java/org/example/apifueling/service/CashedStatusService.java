package org.example.apifueling.service;

import org.example.apifueling.domain.FuelingOrderStatus;
import org.example.apifueling.dto.OrderStatusDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CashedStatusService {

    private static final int TIMEOUT_SECONDS = 60;
    private static final int STEP_MILLIS = 200;
    private final ConcurrentHashMap<UUID, OrderStatusDto> map = new ConcurrentHashMap<>();

    public Flux<OrderStatusDto> getOrderStatusById(OrderStatusDto dto) {
        return Flux.generate(() -> dto, (state, sink) -> {
                    OrderStatusDto updatedDto = getTimedOrderById(dto.getId(), Duration.ofMillis(STEP_MILLIS));
                    sink.next(updatedDto);
                    if (updatedDto.getStatus().equals(FuelingOrderStatus.COMPLETED)
                            || updatedDto.getStatus().equals(FuelingOrderStatus.CANCELED)) {
                        sink.complete();
                    }
                    return updatedDto;
                })
                .log()
                .cast(OrderStatusDto.class);
    }

    private OrderStatusDto getTimedOrderById(UUID id, Duration timeout) {
        OrderStatusDto dto = null;
        LocalDateTime startTime = LocalDateTime.now();
        while (dto == null) {
            if (LocalDateTime.now().isAfter(startTime.plusSeconds(TIMEOUT_SECONDS))) {
                throw new RuntimeException("Fetching order status timeout");
            }
            try {
                Thread.sleep(timeout.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dto = map.remove(id);
        }
        return dto;
    }

    public void addOrder(OrderStatusDto dto) {
        map.put(dto.getId(), dto);
    }
}
