package org.example.apifueling.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "service.kafka.topic")
@Component
data class KafkaTopic(
    var processing: String = "",
    var status: String = ""
)