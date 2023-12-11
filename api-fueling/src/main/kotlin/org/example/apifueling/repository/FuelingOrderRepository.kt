package org.example.apifueling.repository

import org.example.apifueling.domain.FuelingOrder
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FuelingOrderRepository: JpaRepository<FuelingOrder, UUID>