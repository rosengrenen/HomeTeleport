package se.rsrp.hometeleport

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import se.rsrp.hometeleport.commands.*
import se.rsrp.hometeleport.events.*

class Main : JavaPlugin() {
    override fun onEnable() {
        val config = Config(this.dataFolder, BukkitAudiences.create(this))

        // Create default config
        if (!config.configFile.exists()) {
            this.saveResource("config.yml", true)
            logger.info("Copying default config!")
        }

        if (!config.messagesFile.exists()) {
            this.saveResource("messages.yml", true)
            logger.info("Copying default messages!")
        }

        // IMPORTANT: load the config
        config.reload()

        val mainWorld = Bukkit.getWorld("world") ?: throw Error("Could not find main world")
        val teleportManager = TeleportManager(this, config, mainWorld)

        // Register commands
        this.getCommand("home")!!.setExecutor(CommandHome(config, teleportManager))
        this.getCommand("hometeleport")!!.setExecutor(CommandHomeTeleport(config))

        // Register events
        Bukkit.getPluginManager().registerEvents(PlayerLeave(teleportManager), this)
        Bukkit.getPluginManager().registerEvents(PlayerMove(teleportManager), this)
    }
}
