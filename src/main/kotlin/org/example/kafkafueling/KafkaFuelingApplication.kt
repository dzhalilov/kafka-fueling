package org.example.kafkafueling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaFuelingApplication

fun main(args: Array<String>) {
    runApplication<KafkaFuelingApplication>(*args)
}
