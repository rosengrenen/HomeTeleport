package se.rsrp.hometeleport

import io.papermc.lib.PaperLib
import java.util.HashMap
import java.util.UUID
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import se.rsrp.hometeleport.tasks.TeleportTask

class TeleportManager(
    private val plugin: Plugin,
    private val config: Config,
    private val mainWorld: World
) {
    // Keep track of when players last teleported
    private val teleportTimes: HashMap<UUID, Long> = HashMap()

    // Keep track of ongoing teleports
    private val teleportTasks: HashMap<UUID, BukkitTask> = HashMap()

    /*
     * Returns the time left until the player can teleport again.
     */
    fun canTeleportIn(player: Player): Long {
        val playerTeleportTime = teleportTimes[player.uniqueId]
        return if (playerTeleportTime != null) {
            val teleportCooldown = config.teleportCooldown()
            val timeSinceTeleport = System.nanoTime() - playerTeleportTime
            0L.coerceAtLeast(teleportCooldown - timeSinceTeleport)
        } else {
            0L
        }
    }

    /*
     * Checks if the player can teleport yet.
     */
    fun canTeleport(player: Player) = canTeleportIn(player) == 0L && !isTeleporting(player)

    /*
     * Checks if a player is currently teleporting.
     */
    fun isTeleporting(player: Player) = teleportTasks.containsKey(player.uniqueId)

    /*
     * Begins a teleport sequence for a player, starting a count-down task
     */
    fun beginTeleport(player: Player) {
        if (teleportTasks.containsKey(player.uniqueId)) {
            return
        }

        val teleportTask = TeleportTask(config, this, player).runTaskTimer(plugin, 0, 20L)
        teleportTasks[player.uniqueId] = teleportTask
    }

    /*
     * Finishes a teleport sequence, moving the player
     */
    fun finishTeleport(player: Player) {
        val playerId = player.uniqueId

        // Bookkeeping
        teleportTasks.remove(playerId)
        teleportTimes[playerId] = System.nanoTime()

        // Find player bed location, or world spawn location if no bed
        var location = player.bedSpawnLocation
        if (location == null) {
            location = mainWorld.spawnLocation
        }

        PaperLib.teleportAsync(player, location)
        if (config.soundOnTeleport()) {
            player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)
        }

        config.sendMessage(player, messageOnTeleport())
    }

    /*
     * Cancels the teleport of a player
     */
    fun cancelTeleport(player: Player): Boolean {
        val playerId = player.uniqueId
        val teleportTask = teleportTasks.remove(playerId) ?: return false
        teleportTask.cancel()
        config.sendMessage(player, config.messageErrorTeleportCancelled())
        return true
    }

    private fun messageOnTeleport() = config.getMessage("on-teleport", null)
}
