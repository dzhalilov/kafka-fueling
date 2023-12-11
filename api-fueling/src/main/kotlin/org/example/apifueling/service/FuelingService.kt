package org.example.apifueling.service

import org.example.apifueling.domain.FuelingOrder
import org.example.apifueling.dto.FuelingOrderDto
import org.example.apifueling.dto.OrderProcessingDto
import org.example.apifueling.dto.OrderStatusDto
import org.example.apifueling.dto.toEntity
import org.example.apifueling.repository.FuelingOrderRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class FuelingService(val fuelingOrderRepository: FuelingOrderRepository) {

    fun order(dto: FuelingOrderDto): OrderStatusDto {
        val order = dto.toEntity()
        fuelingOrderRepository.save(order)
        return order.toStatusDto()
    }

    fun getStatus(orderId: UUID): OrderStatusDto = fuelingOrderRepository.findById(orderId).get().toStatusDto()
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