package org.example.apifueling.cucumber.helper

import model.OrderProcessingDto
import model.OrderStatusDto

interface KafkaHelper {

    fun addOrderProcessing(message: OrderProcessingDto)

    fun getOrderProcessingList(): List<OrderProcessingDto>

    fun getOrderProcessing(): OrderProcessingDto?

    fun clearOrderProcessingQueue()

    fun sendOrderStatus(orderStatusDto: OrderStatusDto)
}