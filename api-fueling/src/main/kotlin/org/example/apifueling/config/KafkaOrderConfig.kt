package org.example.apifueling.config

import model.OrderProcessingDto
import model.OrderStatusDto
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaOrderConfig(
    private val kafkaProperties: KafkaOrderProperties
) {

    @Bean
    fun producerProcessingFactory(): ProducerFactory<String, OrderProcessingDto> {
        val configProps = HashMap<String, Any>()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.url
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun producerProcessing(): KafkaTemplate<String, OrderProcessingDto> {
        return KafkaTemplate(producerProcessingFactory())
    }

    @Bean
    fun consumerFuelingOrderFactory(): ConsumerFactory<String, OrderStatusDto> {
        val configProps = HashMap<String, Any>()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.url
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = kafkaProperties.statusGroupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        configProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        configProps[JsonDeserializer.TRUSTED_PACKAGES] = kafkaProperties.trustedPackages
//        configProps[JsonDeserializer.USE_TYPE_INFO_HEADERS] = false
//        configProps[JsonDeserializer.VALUE_DEFAULT_TYPE] = OrderStatusDto::class.java
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderStatusDto>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderStatusDto>()
        factory.consumerFactory = consumerFuelingOrderFactory()
        return factory
    }
}