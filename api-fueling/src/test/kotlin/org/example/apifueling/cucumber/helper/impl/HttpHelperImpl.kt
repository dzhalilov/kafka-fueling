package org.example.apifueling.cucumber.helper.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import model.OrderStatusDto
import org.example.apifueling.cucumber.context.ScenarioContext
import org.example.apifueling.cucumber.helper.HttpHelper
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@Component
class HttpHelperImpl(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : HttpHelper {

    private lateinit var state: TestState

    override fun initRequestBuilder(url: String, method: HttpMethod) {
        state = TestState(MockMvcRequestBuilders.request(method, url))
    }

    override fun withHeaders(vararg headers: Pair<String, String>) {
//        state.requestBuilder = state.requestBuilder.apply {
//            headers {
//                headers.forEach { (header, value) ->
//                    it.add(header, value)
//                }
//            }
//        }
    }

    override fun withJsonBody(body: String) {
        state.requestBuilder.apply {
            contentType(APPLICATION_JSON)
            content(body)
        }
    }

    override fun exchangeRequest() {
        state.resultActions = mockMvc.perform(state.requestBuilder)
        val contentAsString = state.resultActions.andReturn().response.contentAsString
        val (id, status) = objectMapper.readValue(contentAsString, OrderStatusDto::class.java)
        ScenarioContext.id.set(id)
        ScenarioContext.status.set(status)
    }

    override fun expectStatus(status: Int) {
        state.resultActions.andExpect { it.response.status shouldBe status }
    }

    override fun expectJsonBody(body: String) {
        state.resultActions.andReturn().response.contentAsString shouldBe body
    }

    override fun expectBodiless() {
        state.resultActions.andReturn().response.contentAsString shouldBe ""
    }

    class TestState(
        var requestBuilder: MockHttpServletRequestBuilder
    ) {
        lateinit var resultActions: ResultActions
    }
}
