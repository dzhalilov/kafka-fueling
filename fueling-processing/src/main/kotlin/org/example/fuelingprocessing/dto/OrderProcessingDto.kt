package org.example.fuelingprocessing.dto

import org.example.fuelingprocessing.domain.FuelType
import java.util.*

data class OrderProcessingDto(
    var id: UUID,
    val clientId: Int,
    val stationId: Int,
    val fuelType: FuelType,
    val quantity: Int,
    val sum: Int
)