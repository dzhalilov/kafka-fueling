package org.example.apifueling.dto

import org.example.apifueling.domain.FuelType
import java.util.*

data class OrderProcessingDto(
    var id: UUID,
    val clientId: Int,
    val stationId: Int,
    val fuelType: FuelType,
    val quantity: Int,
    val sum: Int
)