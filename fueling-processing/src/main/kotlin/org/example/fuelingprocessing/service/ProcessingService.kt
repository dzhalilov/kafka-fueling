package org.example.fuelingprocessing.service

import kotlinx.coroutines.*
import model.FuelingOrderStatus
import model.FuelingOrderStatus.*
import model.OrderProcessingDto
import model.OrderStatusDto
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import service.KafkaStatusService

private val logger = KotlinLogging.logger {}

@Service
class ProcessingService(
    private val kafkaStatusService: KafkaStatusService
) {

    @OptIn(DelicateCoroutinesApi::class)
    @KafkaListener(topics = ["FUELING.PROCESSING"], groupId = "fueling-order")
    fun processingOrder(dto: OrderProcessingDto) {
        GlobalScope.launch {
            var status = CREATED
            while (status != COMPLETED && status != CANCELED) {
                status = processing(status)
                kafkaStatusService.sendOrderStatus(OrderStatusDto(dto.id, status))
                logger.info { "Order ${dto.id} set status: $status" }
            }
        }
    }

    private suspend fun processing(status: FuelingOrderStatus): FuelingOrderStatus {
        delay(3000L)
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