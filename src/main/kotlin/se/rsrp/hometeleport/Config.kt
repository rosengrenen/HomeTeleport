package se.rsrp.hometeleport

import java.io.File
import java.util.function.Function
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class Config(dataFolder: File, private val audiences: BukkitAudiences) {
    val configFile = File(dataFolder, "config.yml")
    val messagesFile = File(dataFolder, "messages.yml")

    companion object {
        const val CONFIG_VERSION = 1
        const val MESSAGES_VERSION = 1
    }

    private var config: FileConfiguration? = null
    private var messages: FileConfiguration? = null

    fun reload() {
        config = YamlConfiguration.loadConfiguration(configFile)
        if (config!!.getInt("config-version") != CONFIG_VERSION) {
            // logger.warning("Your config file is outdated! Please regenerate the
            // config.");
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile)
        if (messages!!.getInt("version") != MESSAGES_VERSION) {
            // logger.warning("Your messages file is outdated! Please regenerate this
            // file.");
        }
    }

    fun teleportCooldown() = config!!.getInt("teleport-cooldown")
    fun secondsToTeleport() = config!!.getInt("seconds-to-teleport")
    fun soundOnTeleport() = config!!.getBoolean("sound-on-teleport")

    fun getMessage(messagePath: String, transformMessage: Function<String, String>?): Component {
        var message =
            messages!!.getString(messagePath) ?: throw Error("Invalid message path $messagePath")
        if (transformMessage != null) {
            message = transformMessage.apply(message)
        }

        return MiniMessage.miniMessage().deserialize(message)
    }

    fun sendMessage(sender: CommandSender, component: Component) =
        audiences.sender(sender).sendMessage(component)
    fun sendMessage(player: Player, component: Component) =
        audiences.player(player).sendMessage(component)

    fun messageErrorMustBePlayer() = getMessage("errors.must-be-player", null)
    fun messageErrorTeleportCancelled() = getMessage("errors.teleport-cancelled", null)
    fun messageErrorNoPermissions() = getMessage("errors.no-permissions", null)
    fun messageConfigReloaded() = getMessage("config-reloaded", null)
}
