package org.example.apifueling.cucumber.helper

import mu.KLogging
import org.example.apifueling.dto.OrderProcessingDto
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Profile("test")
@Component
class KafkaTestListener(
    private val kafkaHelper: KafkaHelper
) {

    private companion object : KLogging()

    @KafkaListener(
        groupId = "fueling-order",
        topics = ["FUELING.PROCESSING"],
        containerFactory = "kafkaTestOrderStatusListenerContainerFactory"
    )
    fun getMessage(message: OrderProcessingDto) {
        kotlin.runCatching {
            logger.info { "Message in Kafka: $message" }
            kafkaHelper.addOrderProcessing(message)
        }.onFailure {
            logger.error { "Error to handle message in Kafka"}
        }
    }
}