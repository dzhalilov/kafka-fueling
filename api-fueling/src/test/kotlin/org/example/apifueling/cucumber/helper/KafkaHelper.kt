package org.example.apifueling.cucumber.helper

import org.example.apifueling.dto.OrderProcessingDto
import org.example.apifueling.dto.OrderStatusDto

interface KafkaHelper {

    fun addOrderProcessing(message: OrderProcessingDto)

    fun getOrderProcessingList(): List<OrderProcessingDto>

    fun getOrderProcessing(): OrderProcessingDto?

    fun clearOrderProcessingQueue()

    fun sendOrderStatus(orderStatusDto: OrderStatusDto)
}