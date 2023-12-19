package org.example.apifueling.cucumber.step

import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.example.apifueling.cucumber.context.ScenarioContext
import org.example.apifueling.cucumber.helper.DbHelper
import org.example.apifueling.cucumber.helper.HttpHelper
import org.example.apifueling.cucumber.helper.KafkaHelper
import org.example.apifueling.domain.FuelingOrderStatus
import org.springframework.http.HttpMethod

class OrderStepDefinition(
    private val dbHelper: DbHelper,
    private val httpHelper: HttpHelper,
    private val kafkaHelper: KafkaHelper
) {

    @Дано("В БД нет заказов")
    fun clearOrders() {
        dbHelper.clearTables("fueling_order")
    }

    @Когда("Пользователь сделал {httpMethod} запрос на создание заказа с телом: {bodyPath}")
    fun createOrder(httpMethod: HttpMethod, bodyPath: String) {
        httpHelper.initRequestBuilder("/api/v1/fueling/order", httpMethod)
        httpHelper.withJsonBody(bodyPath)
        httpHelper.exchangeRequest()
    }

    @Тогда("Пользователь получил ответ со статусом {orderStatus} и с кодом ответа {httpStatus}")
    fun getOrderCreationResponse(orderStatus: FuelingOrderStatus, httpStatus: Int) {
        httpHelper.expectStatus(httpStatus)
        ScenarioContext.status.get() shouldBe orderStatus
    }

    @И("В БД содержится новый заказ со статусом {orderStatus}")
    fun checkSavedOrder(orderStatus: FuelingOrderStatus) {
        dbHelper.getOrderById(ScenarioContext.id.get())?.status shouldBe orderStatus
    }

    @И("Проверяем новый заказ в Kafka")
    fun checkOrder() {
        val messages = kafkaHelper.getOrderProcessing()
        messages shouldNotBe null
        messages?.id shouldBe ScenarioContext.id.get()
    }

    @И("Очистим Kafka")
    fun clearKafka() {
        kafkaHelper.clearOrderProcessingQueue()
    }
}