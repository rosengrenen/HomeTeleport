package se.rsrp.hometeleport.commands;

import net.kyori.adventure.text.Component;
import se.rsrp.hometeleport.Config;
import se.rsrp.hometeleport.TeleportManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandHome implements TabExecutor {
	private final Config config;
	private final TeleportManager teleportManager;

	public CommandHome(Config config, TeleportManager teleportManager) {
		this.config = config;
		this.teleportManager = teleportManager;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			String[] args) {
		if (!sender.hasPermission("hometeleport.commands.home")) {
			config.sendMessage(sender, config.messageErrorNoPermissions());
			return true;
		}

		if (sender instanceof ConsoleCommandSender) {
			config.sendMessage(sender, config.messageErrorMustBePlayer());
			return true;
		}

		Player player = (Player) sender;
		int argsLength = args.length;
		if (argsLength != 0) {
			config.sendMessage(player, messageInvalidSyntax());
		} else {
			if (teleportManager.canTeleport(player)
					|| player.hasPermission("hometeleport.bypasscooldown")) {
				teleportManager.beginTeleport(player);
			} else {
				long timeLeft = teleportManager.canTeleportIn(player);
				config
						.sendMessage(player, messageTeleportCooldown(timeLeft));
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String alias,
			String[] args) {
		return null;
	}

	private Component messageTeleportCooldown(long timeLeft) {
		return config.getMessage("commands.home.teleport-cooldown",
				message -> message.replace("%seconds%", Long.toString(timeLeft)));
	}

	private Component messageInvalidSyntax() {
		return config.getMessage("commands.home.invalid-syntex", null);
	}
}
