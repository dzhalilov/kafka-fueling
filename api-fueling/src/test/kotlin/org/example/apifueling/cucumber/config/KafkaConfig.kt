package org.example.apifueling.cucumber.config

import model.OrderProcessingDto
import model.OrderStatusDto
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Profile("test")
@TestConfiguration
class KafkaConfig(
    private val env: ConfigurableEnvironment
) {
    val url = env.getProperty("spring.kafka.producer.bootstrap-servers")
        ?.substringAfter("//")

    private fun listenerProperties(groupId: String): Map<String, Any?> {

        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to url,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
        )
        return props
    }

    private fun <T> testKafkaConsumerFactory(groupId: String, jsonDeserializer: Class<T>): ConsumerFactory<String, T> =
        DefaultKafkaConsumerFactory(
            listenerProperties(groupId),
            StringDeserializer(),
            JsonDeserializer(jsonDeserializer)
        )

    @Bean
    fun kafkaTestProcessingListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderProcessingDto> =
        ConcurrentKafkaListenerContainerFactory<String, OrderProcessingDto>().apply {
            consumerFactory = testKafkaConsumerFactory(
                groupId = "fueling-order",
                jsonDeserializer = OrderProcessingDto::class.java
            )
        }

    @Bean
    fun kafkaTestOrderStatusListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderStatusDto> =
        ConcurrentKafkaListenerContainerFactory<String, OrderStatusDto>().apply {
            consumerFactory = testKafkaConsumerFactory(
                groupId = "fueling-status",
                jsonDeserializer = OrderStatusDto::class.java
            )
        }

    @Bean
    fun producerFuelingStatusFactory(): ProducerFactory<String, OrderStatusDto> {
        val configProps = HashMap<String, Any>()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = listOf(url)
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun producerFuelingStatus(): KafkaTemplate<String, OrderStatusDto> {
        return KafkaTemplate(producerFuelingStatusFactory())
    }
}
