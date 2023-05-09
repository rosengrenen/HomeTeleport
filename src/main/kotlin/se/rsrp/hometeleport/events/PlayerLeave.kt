package se.rsrp.hometeleport.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import se.rsrp.hometeleport.TeleportManager

class PlayerLeave(private val teleportManager: TeleportManager) : Listener {
    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        teleportManager.cancelTeleport(event.player)
    }
}
