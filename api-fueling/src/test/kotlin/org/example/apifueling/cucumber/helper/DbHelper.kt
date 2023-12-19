package org.example.apifueling.cucumber.helper

import org.example.apifueling.domain.FuelingOrder
import java.util.*

interface DbHelper {

    fun clearTables(vararg tableNames: String)

    fun insert(sql: String)

    fun insert(sql: String, vararg args: Any?)

    fun update(sql: String)

    fun update(sql: String, vararg args: Any?)

    fun delete(sql: String)

    fun insertNamedParameter(sql: String, params: Map<String, Any?>): Int

    fun select(sql: String): List<Any>

    fun getOrderById(id: UUID): FuelingOrder?
}