package se.rsrp.hometeleport;

import java.io.File;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Config {
	public final File configFile;
	public final File messagesFile;
	private static final int CONFIG_VERSION = 1;
	private static final int MESSAGES_VERSION = 1;

	private FileConfiguration config;
	private FileConfiguration messages;

	private static final MiniMessage miniMessage = MiniMessage.miniMessage();

	private final BukkitAudiences audiences;

	public Config(File dataFolder, BukkitAudiences audiences) {
		this.configFile = new File(dataFolder, "config.yml");
		this.messagesFile = new File(dataFolder, "messages.yml");
		this.audiences = audiences;
	}

	public void reload() {
		config = YamlConfiguration.loadConfiguration(configFile);
		if (config.getInt("config-version") != CONFIG_VERSION) {
			// logger.warning("Your config file is outdated! Please regenerate the
			// config.");
		}

		messages = YamlConfiguration.loadConfiguration(messagesFile);
		if (messages.getInt("version") != MESSAGES_VERSION) {
			// logger.warning("Your messages file is outdated! Please regenerate this
			// file.");
		}
	}

	public int teleportCooldown() {
		return config.getInt("teleport-cooldown");
	}

	public int secondsToTeleport() {
		return config.getInt("seconds-to-teleport");
	}

	public boolean soundOnTeleport() {
		return config.getBoolean("sound-on-teleport");
	}

	public Component getMessage(String messagePath, Function<String, String> transformMessage) {
		String message = messages.getString(messagePath);
		if (transformMessage != null) {
			message = transformMessage.apply(message);
		}

		return miniMessage.deserialize(message);
	}

	public void sendMessage(CommandSender sender, Component component) {
		audiences.sender(sender).sendMessage(component);
	}

	public void sendMessage(Player player, Component component) {
		audiences.player(player).sendMessage(component);
	}

	public Component messageErrorMustBePlayer() {
		return getMessage("errors.must-be-player", null);
	}

	public Component messageErrorTeleportCancelled() {
		return getMessage("errors.teleport-cancelled", null);
	}

	public Component messageErrorNoPermissions() {
		return getMessage("errors.no-permissions", null);
	}

	public Component messageConfigReloaded() {
		return getMessage("config-reloaded", null);
	}
}
