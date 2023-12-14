package org.example.apifueling.service

import model.OrderProcessingDto
import model.OrderStatusDto
import mu.KotlinLogging
import org.example.apifueling.domain.FuelingOrder
import org.example.apifueling.dto.FuelingOrderDto
import org.example.apifueling.dto.toEntity
import org.example.apifueling.repository.FuelingOrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import service.KafkaOrderService

private val logger = KotlinLogging.logger {}
@Service
class FuelingService(
    private val fuelingOrderRepository: FuelingOrderRepository,
    private val kafkaOrderService: KafkaOrderService
) {

    @Transactional
    fun order(dto: FuelingOrderDto): OrderStatusDto {
        val order = dto.toEntity()
        fuelingOrderRepository.save(order)
        kafkaOrderService.sendOrderProcessing(order.toProcessingDto())
        val statusDto = order.toStatusDto()
        logger.info { "Order sent to processing: $statusDto" }
        return statusDto
    }
}

fun FuelingOrder.toStatusDto() = OrderStatusDto(
    id = id!!,
    status = status
)

fun FuelingOrder.toProcessingDto() = OrderProcessingDto(
    id = id!!,
    clientId = clientId,
    stationId = stationId,
    fuelType = fuelType,
    quantity = quantity,
    sum = sum
)