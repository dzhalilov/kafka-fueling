package org.example.apifueling.service

import org.example.apifueling.config.KafkaTopic
import org.example.apifueling.dto.OrderProcessingDto
import org.example.apifueling.dto.OrderStatusDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProcessingService(
    private val kafkaProcessingTemplate: KafkaTemplate<String, OrderProcessingDto>,
    private val kafkaTopic: KafkaTopic,
    private val orderStatusService: OrderStatusService,
) {

    fun processing(dto: OrderProcessingDto) {
        kafkaProcessingTemplate.send(kafkaTopic.processing, dto)
    }

    @KafkaListener(topics = ["#{kafkaTopic.status}"], groupId = "fueling-status")
    fun updateStatusOrder(dto: OrderStatusDto) {
        orderStatusService.updateStatusOrder(dto)
    }
}