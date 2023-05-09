package se.rsrp.hometeleport.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import se.rsrp.hometeleport.Config
import se.rsrp.hometeleport.TeleportManager

class CommandHome(private val config: Config, private val teleportManager: TeleportManager) :
    TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (!sender.hasPermission("hometeleport.commands.home")) {
            config.sendMessage(sender, config.messageErrorNoPermissions())
            return true
        }

        if (sender is ConsoleCommandSender) {
            config.sendMessage(sender, config.messageErrorMustBePlayer())
            return true
        }

        val player = sender as Player
        if (args?.size != 0) {
            config.sendMessage(player, messageInvalidSyntax())
        } else {
            if (
                teleportManager.canTeleport(player) ||
                    player.hasPermission("hometeleport.ignorecooldown")
            ) {
                teleportManager.beginTeleport(player)
            } else {
                val timeLeft = teleportManager.canTeleportIn(player)
                config.sendMessage(player, messageTeleportCooldown(timeLeft))
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        return null
    }

    private fun messageTeleportCooldown(timeLeft: Long) =
        config.getMessage("commands.home.teleport-cooldown") { message ->
            message.replace("%seconds%", timeLeft.toString())
        }

    private fun messageInvalidSyntax() = config.getMessage("commands.home.invalid-syntax", null)
}
