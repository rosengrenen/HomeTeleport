package se.rsrp.hometeleport.tasks

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import se.rsrp.hometeleport.Config
import se.rsrp.hometeleport.TeleportManager

class TeleportTask(
    private val config: Config,
    private val teleportManager: TeleportManager,
    private val player: Player
) : BukkitRunnable() {
    private var secondsUntilTeleport = config.secondsToTeleport()

    override fun run() {
        if (secondsUntilTeleport == 0) {
            teleportManager.finishTeleport(player)
            this.cancel()
        } else {
            var showMessage = false
            if (secondsUntilTeleport >= 10) {
                showMessage = secondsUntilTeleport % 10 == 0
            } else if (secondsUntilTeleport == 5) {
                showMessage = true
            } else if (secondsUntilTeleport <= 3) {
                showMessage = true
            }

            if (showMessage) {
                config.sendMessage(player, messageTeleportingIn(secondsUntilTeleport))
            }

            secondsUntilTeleport--
        }
    }

    private fun messageTeleportingIn(timeLeft: Int) =
        config.getMessage("teleporting-in") { message ->
            message.replace("%seconds%", timeLeft.toString())
        }
}
