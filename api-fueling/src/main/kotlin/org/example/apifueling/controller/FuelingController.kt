package org.example.apifueling.controller

import org.example.apifueling.dto.FuelingOrderDto
import org.example.apifueling.dto.OrderStatusDto
import org.example.apifueling.service.FuelingService
import org.example.apifueling.service.OrderStatusService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

//@RestController
//@RequestMapping("/api/v1/fueling")
//class FuelingController(
//    private val fuelingService: FuelingService,
//    private val orderStatusService: OrderStatusService
//) {
//
//    @PostMapping("/order")
//    fun order(@RequestBody dto: FuelingOrderDto): OrderStatusDto = fuelingService.order(dto)
//
//    @GetMapping("/status/{orderId}")
//    fun getStatus(@PathVariable orderId: UUID): OrderStatusDto = orderStatusService.getStatus(orderId)
//}