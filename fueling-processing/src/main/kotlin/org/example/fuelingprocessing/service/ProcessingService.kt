package org.example.fuelingprocessing.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import model.FuelingOrderStatus
import model.FuelingOrderStatus.*
import model.OrderProcessingDto
import model.OrderStatusDto
import mu.KotlinLogging
import org.example.fuelingprocessing.config.KafkaProcessingProperties
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ProcessingService(
    private val kafkaProcessingProperties: KafkaProcessingProperties,
    private val producerFuelingStatus: KafkaTemplate<String, OrderStatusDto>,
) {

    @KafkaListener(
        topics = ["#{kafkaProcessingProperties.processing}"],
        groupId = "#{kafkaProcessingProperties.orderGroupId}"
    )
    fun processingOrder(dto: OrderProcessingDto) {
        runBlocking {
            var status = CREATED
            while (status != COMPLETED && status != CANCELED) {
                delay(3000L)
                status = processing(status)
                producerFuelingStatus.send(kafkaProcessingProperties.status, OrderStatusDto(dto.id, status))
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