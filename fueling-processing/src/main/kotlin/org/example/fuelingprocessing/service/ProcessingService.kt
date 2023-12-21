package org.example.fuelingprocessing.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.example.fuelingprocessing.config.KafkaTopic
import org.example.fuelingprocessing.domain.FuelingOrderStatus
import org.example.fuelingprocessing.domain.FuelingOrderStatus.*
import org.example.fuelingprocessing.dto.OrderProcessingDto
import org.example.fuelingprocessing.dto.OrderStatusDto
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ProcessingService(
    private val kafkaTopic: KafkaTopic,
    private val kafkaOrderTemplate: KafkaTemplate<String, OrderStatusDto>,
) {

    @KafkaListener(topics = ["#{kafkaTopic.processing}"], groupId = "fueling-order")
    fun processingOrder(dto: OrderProcessingDto) {
        GlobalScope.launch {
            var status = CREATED
            while (status != COMPLETED && status != CANCELED) {
                delay(3000L)
                status = processing(status)
                kafkaOrderTemplate.send(kafkaTopic.status, OrderStatusDto(dto.id, status))
                logger.info { "Order ${dto.id} set status: $status" }
            }
        }
    }

    private fun processing(status: FuelingOrderStatus): FuelingOrderStatus {
        if (randomlyCancel()) return CANCELED
        return when (status) {
            CREATED -> PROCESSING
            PROCESSING -> FUELLING
            FUELLING -> CHANGE_DUE
            CHANGE_DUE -> COMPLETED
            else -> CANCELED
        }
    }

    private fun randomlyCancel() = (0..40).random() < 1
}