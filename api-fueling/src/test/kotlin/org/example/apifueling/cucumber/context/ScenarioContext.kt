package org.example.apifueling.cucumber.context

import model.FuelingOrderStatus
import java.util.UUID

object ScenarioContext {
    val id: ThreadLocal<UUID> = ThreadLocal()
    val status: ThreadLocal<FuelingOrderStatus> = ThreadLocal()
}