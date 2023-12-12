package org.example.fuelingprocessing.dto

import org.example.fuelingprocessing.domain.FuelingOrderStatus
import java.util.*

data class OrderStatusDto(
    val id: UUID,
    val status: FuelingOrderStatus
)
