package org.example.fuelingprocessing.service

import kotlinx.coroutines.*
import model.FuelingOrderStatus
import model.FuelingOrderStatus.*
import model.OrderProcessingDto
import model.OrderStatusDto
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import service.KafkaStatusService
import service.ListenerInterface

private val logger = KotlinLogging.logger {}

@Service
class ProcessingService(context: ApplicationContext): ListenerInterface<OrderProcessingDto> {

    private val kafkaStatusService: KafkaStatusService by lazy { context.getBean(KafkaStatusService::class.java) }

    @OptIn(DelicateCoroutinesApi::class)
    override fun getMessage(dto: OrderProcessingDto) {
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