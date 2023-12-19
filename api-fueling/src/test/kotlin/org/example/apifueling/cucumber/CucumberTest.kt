package org.example.apifueling.cucumber

import io.cucumber.spring.CucumberContextConfiguration
import org.example.apifueling.cucumber.config.KafkaConfig
import org.example.apifueling.cucumber.config.KafkaContainerInitializer
import org.example.apifueling.cucumber.config.PostgresContainerInitializer
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.TestExecutionListeners

@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [
        KafkaConfig::class
    ]
)
@ContextConfiguration(
    initializers = [
        PostgresContainerInitializer::class,
        KafkaContainerInitializer::class
    ]
)
@CucumberContextConfiguration
@AutoConfigureMockMvc
@TestExecutionListeners(
    listeners = [CucumberTest::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Suppress("unused")
class CucumberTest : TestExecutionListener