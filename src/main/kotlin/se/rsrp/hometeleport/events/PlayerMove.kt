package se.rsrp.hometeleport.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import se.rsrp.hometeleport.TeleportManager

class PlayerMove(private val teleportManager: TeleportManager) : Listener {
    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        if (!teleportManager.isTeleporting(player)) {
            return
        }

        val movedX = event.from.blockX != (event.to?.blockX ?: event.from.blockX)
        val movedY = event.from.blockY != (event.to?.blockY ?: event.from.blockY)
        val movedZ = event.from.blockZ != (event.to?.blockZ ?: event.from.blockZ)
        if (movedX || movedY || movedZ) {
            teleportManager.cancelTeleport(player)
        }
    }
}
