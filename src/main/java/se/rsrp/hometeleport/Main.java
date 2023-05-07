package se.rsrp.hometeleport;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import se.rsrp.hometeleport.commands.*;
import se.rsrp.hometeleport.events.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
	private final Logger logger = this.getLogger();

	@Override
	public void onEnable() {
		Config config = new Config(this.getDataFolder(), BukkitAudiences.create(this));

		// Create default config
		if (!config.configFile.exists()) {
			this.saveResource("config.yml", true);
			logger.info("Copying default config!");
		}

		if (!config.messagesFile.exists()) {
			this.saveResource("messages.yml", true);
			logger.info("Copying default messages!");
		}

		// IMPORTANT: load the config
		config.reload();

		TeleportManager teleportManager = new TeleportManager(this, config, Bukkit.getWorld("world"));

		// Register commands
		this.getCommand("home").setExecutor(new CommandHome(config, teleportManager));
		this.getCommand("hometeleport").setExecutor(new CommandHomeTeleport(config));

		// Register events
		Bukkit.getPluginManager().registerEvents(new PlayerLeave(teleportManager), this);
		Bukkit.getPluginManager().registerEvents(new PlayerMove(teleportManager), this);
	}
}
