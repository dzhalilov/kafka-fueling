package org.example.apifueling.service

import model.OrderStatusDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ProcessingService(private val orderStatusService: OrderStatusService) {

    @KafkaListener(topics = ["FUELING.STATUS"], groupId = "fueling-status")
    fun updateStatusOrder(dto: OrderStatusDto) = orderStatusService.updateStatusOrder(dto)

}