package org.example.fuelingprocessing.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "service.kafka.topic")
@Component
data class KafkaProcessingProperties(
    var processing: String = "FUELING.PROCESSING",
    var status: String = "FUELING.STATUS",
    var url: String = "localhost:9093",
    var orderGroupId: String = "fueling-order",
    var statusGroupId: String = "fueling-status",
    var trustedPackages: String = "model"
)