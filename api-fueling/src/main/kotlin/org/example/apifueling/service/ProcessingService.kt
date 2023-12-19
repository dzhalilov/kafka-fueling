package org.example.apifueling.service

import model.OrderProcessingDto
import model.OrderStatusDto
import org.example.apifueling.config.KafkaOrderProperties
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProcessingService(
    private val producerProcessing: KafkaTemplate<String, OrderProcessingDto>,
    private val kafkaOrderProperties: KafkaOrderProperties,
    private val orderStatusService: OrderStatusService,
) {

    fun processing(dto: OrderProcessingDto) {
        producerProcessing.send(kafkaOrderProperties.processing, dto)
    }

    @KafkaListener(
        topics = ["#{kafkaOrderProperties.status}"],
        groupId = "#{kafkaOrderProperties.statusGroupId}"
    )
    fun updateStatusOrder(dto: OrderStatusDto) {
        orderStatusService.updateStatusOrder(dto)
    }
}