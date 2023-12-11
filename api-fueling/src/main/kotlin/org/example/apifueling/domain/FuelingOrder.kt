package org.example.apifueling.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
class FuelingOrder(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID,
    val clientId: Int,
    val stationId: Int,
    @Enumerated(value = EnumType.STRING)
    val fuelType: FuelType,
    val quantity: Int,
    val sum: Int,
    @CreationTimestamp
    val createdAt: Instant? = null,
    @UpdateTimestamp
    val updatedAt: Instant? = null,
    @Enumerated(value = EnumType.STRING)
    var status: FuelingOrderStatus = FuelingOrderStatus.CREATED
)

enum class FuelingOrderStatus {
    CREATED,
    PROCESSING,
    FUELLING,
    CHANGE_DUE,
    COMPLETED,
    CANCELED
}

enum class FuelType {
    AI_92,
    AI_95,
    AI_98,
    AI_100,
    DIESEL
}
