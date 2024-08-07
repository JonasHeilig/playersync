package de.jonasheilig.playersync

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.PreparedStatement
import java.util.Base64

class PlayerDataHandler : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId.toString()

        Bukkit.getScheduler().runTaskAsynchronously(Playersync.instance) {
            val statement: PreparedStatement = Database.connection.prepareStatement(
                "SELECT inventory, xp FROM player_data WHERE uuid = ?"
            )
            statement.setString(1, uuid)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                val inventoryData = resultSet.getString("inventory")
                val xp = resultSet.getInt("xp")

                val inputStream = ByteArrayInputStream(Base64.getDecoder().decode(inventoryData))
                val dataInput = BukkitObjectInputStream(inputStream)

                val inventory = arrayOfNulls<ItemStack>(dataInput.readInt())
                for (i in inventory.indices) {
                    inventory[i] = dataInput.readObject() as ItemStack
                }

                Bukkit.getScheduler().runTask(Playersync.instance) {
                    player.inventory.contents = inventory
                    player.totalExperience = xp
                }

                dataInput.close()
                inputStream.close()
            }

            resultSet.close()
            statement.close()
        }
    }

}
