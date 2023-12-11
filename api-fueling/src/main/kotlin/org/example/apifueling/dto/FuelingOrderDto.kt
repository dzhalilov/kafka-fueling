package org.example.apifueling.dto

import org.example.apifueling.domain.FuelType
import org.example.apifueling.domain.FuelingOrder
import java.util.*

data class FuelingOrderDto(
    var clientId: Int,
    var stationId: Int,
    var fuelType: FuelType,
    var quantity: Int,
    var sum: Int,
)

fun FuelingOrderDto.toEntity() = FuelingOrder(
    clientId = clientId,
    stationId = stationId,
    fuelType = fuelType,
    quantity = quantity,
    sum = sum,
    id = UUID.randomUUID()
)