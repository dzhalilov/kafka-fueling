package org.example.apifueling.cucumber.step

import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import io.kotest.matchers.shouldBe
import model.FuelingOrderStatus
import model.OrderStatusDto
import mu.KLogging
import org.example.apifueling.cucumber.context.ScenarioContext
import org.example.apifueling.cucumber.helper.DbHelper
import org.example.apifueling.cucumber.helper.KafkaHelper
import java.util.*

class ChangeOrderStatusStepDefinition(
    private val dbHelper: DbHelper,
    private val kafkaHelper: KafkaHelper
) {

    private companion object : KLogging()

    @Дано("В БД есть заказ с id {uuid} и статусом {orderStatus}")
    fun createOrder(uuid: UUID, orderStatus: FuelingOrderStatus) {
        ScenarioContext.id.set(uuid)
        dbHelper.insert(
            "INSERT INTO fueling_order(id, client_id, station_id, fuel_type, quantity, sum, status) " +
                    "VALUES ('${uuid}', 124, 90001, 'AI_95', 10000, 530, '${orderStatus}')"
        )
    }

    @Когда("Приходит новый статус {orderStatus} для заказа с id {uuid}")
    fun sendOrderStatusDtoThroughKafka(orderStatus: FuelingOrderStatus, uuid: UUID) {
        logger.info { "Send order status $orderStatus for order $uuid" }
        kafkaHelper.sendOrderStatus(OrderStatusDto(uuid, orderStatus))
    }

    @И("Подождем {int} сек.")
    fun sleep(seconds: Int) {
        logger.info { "Wait $seconds seconds" }
        Thread.sleep(seconds * 1000L)
    }

    @Тогда("Проверим, что статус в БД обновиться на {orderStatus}")
    fun checkOrderStatusInDb(orderStatus: FuelingOrderStatus) {
        val fuelingOrder = dbHelper.getOrderById(ScenarioContext.id.get())
        logger.info { "Fueling order: ${fuelingOrder?.id} with status: ${fuelingOrder?.status}" }
        fuelingOrder?.status shouldBe orderStatus
    }

    @И("Очистим БД")
    fun clearDb() {
        dbHelper.clearTables("fueling_order")
    }

}