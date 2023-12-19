package org.example.apifueling.cucumber.config

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

class KafkaContainerInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        KafkaContainer(DockerImageName("confluentinc/cp-kafka:7.4.0")).apply {
            start()
            TestPropertyValues
                .of("spring.kafka.producer.bootstrap-servers=${bootstrapServers}")
                .and("spring.kafka.consumer.bootstrap-servers=${bootstrapServers}")
                .and("spring.kafka.consumer.auto-offset-reset=earliest")
                .applyTo(applicationContext.environment)}
    }
}