package de.jonasheilig.playersync

import org.bukkit.plugin.java.JavaPlugin

class Playersync : JavaPlugin() {

    companion object {
        lateinit var instance: Playersync
            private set
    }

    override fun onEnable() {
        // Plugin startup logic
        instance = this

        saveDefaultConfig()
        Database.connect(
            config.getString("database.host")!!,
            config.getString("database.port")!!,
            config.getString("database.name")!!,
            config.getString("database.username")!!,
            config.getString("database.password")!!
        )

        server.pluginManager.registerEvents(PlayerDataHandler(), this)

        server.logger.info("Playersync enabled")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        Database.disconnect()
    }
}
