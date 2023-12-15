package org.example.apifueling.service

import model.OrderStatusDto
import org.springframework.stereotype.Service
import service.ListenerInterface

@Service
class OrderStatusListenerService(private val orderStatusService: OrderStatusService): ListenerInterface<OrderStatusDto> {

    override fun getMessage(dto: OrderStatusDto) {
        orderStatusService.updateStatusOrder(dto)
    }
}