package org.example.apifueling.cucumber.config

import mu.KLogging
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class PostgresContainerInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object : KLogging()

    override fun initialize(applicationContext: ConfigurableApplicationContext)
    {
        PostgreSQLContainer(
            "postgres:15-alpine"
        ).apply {
            start()
            TestPropertyValues
                .of("spring.datasource.url=${jdbcUrl}",)
                .and("spring.datasource.username=${username}")
                .and("spring.datasource.password=${password}")
                .applyTo(applicationContext.environment)
        }.also {
            logger.info { "spring.datasource.url: ${it.jdbcUrl}" }
            logger.info { "spring.datasource.username: ${it.username}" }
            logger.info { "spring.datasource.password: ${it.password}" }
        }
    }
}