package org.example.apifueling.domain

import jakarta.persistence.*
import model.FuelType
import model.FuelingOrderStatus
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
class FuelingOrder(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
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
