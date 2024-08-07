package de.jonasheilig.playersync

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {

    lateinit var connection: Connection

    fun connect(host: String, port: String, database: String, user: String, password: String) {
        val url = "jdbc:mysql://$host:$port/$database"
        try {
            connection = DriverManager.getConnection(url, user, password)
            val statement = connection.createStatement()
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS player_data (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "inventory TEXT," +
                        "xp INT" +
                        ")"
            )
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            if (!connection.isClosed) {
                connection.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
