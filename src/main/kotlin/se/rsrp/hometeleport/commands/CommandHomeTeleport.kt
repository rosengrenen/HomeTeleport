package se.rsrp.hometeleport.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import se.rsrp.hometeleport.Config

class CommandHomeTeleport(private val config: Config) : TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (!sender.hasPermission("hometeleport.command")) {
            config.sendMessage(sender, config.messageErrorNoPermissions())
            return true
        }

        if (args?.size == 1) {
            if (args[0] == "reload") {
                if (sender.hasPermission("hometeleport.reload")) {
                    config.reload()
                    config.sendMessage(sender, config.messageConfigReloaded())
                } else {
                    config.sendMessage(sender, config.messageErrorNoPermissions())
                }
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
}
