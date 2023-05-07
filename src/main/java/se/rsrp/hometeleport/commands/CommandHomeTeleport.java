package se.rsrp.hometeleport.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import se.rsrp.hometeleport.Config;

import java.util.Collections;
import java.util.List;

public class CommandHomeTeleport implements TabExecutor {
	private final Config config;

	public CommandHomeTeleport(Config config) {
		this.config = config;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (!sender.hasPermission("hometeleport.command")) {
			config.sendMessage(sender, config.messageErrorNoPermissions());
			return true;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("hometeleport.reload")) {
					config.reload();
					config.sendMessage(sender, config.messageConfigReloaded());
				} else {
					config.sendMessage(sender, config.messageErrorNoPermissions());
				}
			}
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
			@NotNull String[] args) {
		if (args.length == 1) {
			return Collections.singletonList("reload");
		}

		return null;
	}

}
