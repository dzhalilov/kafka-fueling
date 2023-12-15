package org.example.apifueling.dto

import model.FuelType
import org.example.apifueling.domain.FuelingOrder

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
    sum = sum
)