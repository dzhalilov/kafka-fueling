package org.example.apifueling.cucumber.helper.impl

import model.OrderProcessingDto
import model.OrderStatusDto
import mu.KLogging
import org.example.apifueling.cucumber.helper.KafkaHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@Component
class KafkaHelperImpl(
    private val kafkaTestProducerTemplate: KafkaTemplate<String, OrderStatusDto>
): KafkaHelper {

    @Value("\${service.kafka.topic.status}")
    private lateinit var statusTopic: String

    private companion object : KLogging() {
        val orderProcessingQueue: LinkedBlockingQueue<OrderProcessingDto> = LinkedBlockingQueue()
    }

    override fun addOrderProcessing(message: OrderProcessingDto) {
        orderProcessingQueue.add(message)
    }

    override fun getOrderProcessingList(): List<OrderProcessingDto> = orderProcessingQueue.toList()

    override fun getOrderProcessing(): OrderProcessingDto? = orderProcessingQueue.poll(2, TimeUnit.SECONDS)

    override fun clearOrderProcessingQueue() {
        orderProcessingQueue.clear()
    }

    override fun sendOrderStatus(orderStatusDto: OrderStatusDto) {
        kafkaTestProducerTemplate.send(statusTopic, orderStatusDto)
    }
}