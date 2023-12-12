package org.example.apifueling.service

import mu.KotlinLogging
import org.example.apifueling.dto.OrderStatusDto
import org.example.apifueling.repository.FuelingOrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class OrderStatusService(
    private val fuelingOrderRepository: FuelingOrderRepository
) {

    @Transactional(readOnly = true)
    fun getStatus(orderId: UUID): OrderStatusDto = fuelingOrderRepository.findById(orderId).get().toStatusDto()

    @Transactional
    fun updateStatusOrder(dto: OrderStatusDto) {
        val order = fuelingOrderRepository.findById(dto.id).get()
        order.status = dto.status
        logger.info { "Updated order ${order.id} status: ${dto.status}" }
    }
}