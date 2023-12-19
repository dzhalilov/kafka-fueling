package org.example.apifueling.cucumber.helper

import org.springframework.http.HttpMethod

interface HttpHelper {

    fun initRequestBuilder(url: String, method: HttpMethod)

    fun withHeaders(vararg headers: Pair<String, String>)

    fun withJsonBody(body: String)

    fun exchangeRequest()

    fun expectStatus(status: Int)

    fun expectJsonBody(body: String)

    fun expectBodiless()

}