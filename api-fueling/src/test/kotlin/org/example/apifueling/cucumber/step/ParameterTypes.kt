package org.example.apifueling.cucumber.step

import io.cucumber.java.ParameterType
import model.FuelingOrderStatus
import org.example.apifueling.cucumber.helper.FixtureLoadHelper
import org.springframework.http.HttpMethod
import java.util.*

@Suppress("unused")
class ParameterTypes(
    private val fixtureLoadHelper: FixtureLoadHelper
) {

    @ParameterType("(.+.(json))")
    fun bodyPath(value: String) = fixtureLoadHelper.loadAsString(value)

    @ParameterType("GET|POST|PUT|DELETE")
    fun httpMethod(value: String) = HttpMethod.valueOf(value)

    @ParameterType("\\d+{3}")
    fun httpStatus(value: String) = value.toInt()

    @ParameterType("CREATED|PROCESSING|FUELLING|CHANGE_DUE|COMPLETED|CANCELED")
    fun orderStatus(value: String) = FuelingOrderStatus.valueOf(value)

    @ParameterType("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
    fun uuid(value: String) = UUID.fromString(value)
}