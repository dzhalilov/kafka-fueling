package org.example.apifueling.cucumber.helper.impl

import model.FuelType
import model.FuelingOrderStatus
import org.example.apifueling.cucumber.helper.DbHelper
import org.example.apifueling.domain.FuelingOrder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.test.jdbc.JdbcTestUtils
import java.sql.ResultSet
import java.util.*

@Component
class DbHelperImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
): DbHelper {

    private val orderRowMapper: FuelingOrderRowMapper = FuelingOrderRowMapper()

    override fun clearTables(vararg tableNames: String) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, *tableNames)
    }

    override fun insert(sql: String) {
        jdbcTemplate.update(sql)
    }

    override fun insert(sql: String, vararg args: Any?) {
        jdbcTemplate.update(sql, *args)
    }

    override fun update(sql: String) {
        jdbcTemplate.update(sql)
    }

    override fun update(sql: String, vararg args: Any?) {
        jdbcTemplate.update(sql, *args)
    }

    override fun delete(sql: String) {
        jdbcTemplate.execute(sql)
    }

    override fun insertNamedParameter(sql: String, params: Map<String, Any?>): Int =
        namedParameterJdbcTemplate.update(sql, params)

    override fun select(sql: String): List<Any> = jdbcTemplate.queryForList(sql)

    override fun getOrderById(id: UUID): FuelingOrder? =
        jdbcTemplate.queryForObject("SELECT * FROM fueling_order where id = '$id'", orderRowMapper)
}

class FuelingOrderRowMapper : RowMapper<FuelingOrder> {
    override fun mapRow(rs: ResultSet, rowNum: Int): FuelingOrder {
        val id = UUID.fromString(rs.getString("id"))
        val clientId = rs.getInt("client_id")
        val stationId = rs.getInt("station_id")
        val fuelType = FuelType.valueOf(rs.getString("fuel_type"))
        val quantity = rs.getInt("quantity")
        val sum = rs.getInt("sum")
        val status = FuelingOrderStatus.valueOf(rs.getString("status"))
        val createdAt = rs.getTimestamp("created_at")?.toInstant()
        val updatedAt = rs.getTimestamp("updated_at")?.toInstant()
        return FuelingOrder(id, clientId, stationId, fuelType, quantity, sum, createdAt, updatedAt, status)
    }
}