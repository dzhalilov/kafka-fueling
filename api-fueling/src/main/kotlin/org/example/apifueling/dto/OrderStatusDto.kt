package org.example.apifueling.dto

import org.example.apifueling.domain.FuelingOrderStatus
import java.util.*

data class OrderStatusDto(
    val id: UUID,
    val status: FuelingOrderStatus
)
