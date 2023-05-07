package se.rsrp.hometeleport;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import se.rsrp.hometeleport.tasks.TeleportTask;

public class TeleportManager {
	private Plugin plugin;
	private Config config;
	private World mainWorld;

	// Keep track of when players last teleported
	public final HashMap<UUID, Long> teleportTimes = new HashMap<>();

	// Keep track of ongoing teleports
	public final HashMap<UUID, BukkitTask> teleportTasks = new HashMap<>();

	public TeleportManager(Plugin plugin, Config config, World mainWorld) {
		this.plugin = plugin;
		this.config = config;
		this.mainWorld = mainWorld;
	}

	/*
	 * Returns the time left until the player can teleport again.
	 */
	public long canTeleportIn(Player player) {
		Long playerTeleportTime = teleportTimes.get(player.getUniqueId());
		if (playerTeleportTime != null) {
			long teleportCooldown = config.teleportCooldown();
			long timeSinceTeleport = System.nanoTime() - playerTeleportTime;
			return Math.max(0, teleportCooldown - timeSinceTeleport);
		} else {
			return 0;
		}
	}

	/*
	 * Checks if the player can teleport yet.
	 */
	public boolean canTeleport(Player player) {
		return canTeleportIn(player) == 0 && !isTeleporting(player);
	}

	public boolean isTeleporting(Player player) {
		return teleportTasks.containsKey(player.getUniqueId());
	}

	/*
	 * Begins a teleport sequence for a player, starting a count-down task
	 */
	public void beginTeleport(Player player) {
		UUID playerId = player.getUniqueId();
		if (teleportTasks.containsKey(playerId)) {
			return;
		}

		BukkitTask teleportTask = new TeleportTask(config, this, player)
				.runTaskTimer(plugin, 0, 20L);
		teleportTasks.put(playerId, teleportTask);
	}

	/*
	 * Finishes a teleport sequence, moving the player
	 */
	public void finishTeleport(Player player) {
		UUID playerId = player.getUniqueId();

		// Book-keeping
		teleportTasks.remove(playerId);
		teleportTimes.put(playerId, System.nanoTime());

		// Find player bed location, or world spawn location if no bed
		Location location = player.getBedSpawnLocation();
		if (location == null) {
			location = mainWorld.getSpawnLocation();
		}

		PaperLib.teleportAsync(player, location);
		if (config.soundOnTeleport()) {
			player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
		}

		config.sendMessage(player, messageOnTeleport());
	}

	/*
	 * Cancels the teleport of a player
	 */
	public boolean cancelTeleport(Player player) {
		UUID playerId = player.getUniqueId();
		BukkitTask teleportTask = teleportTasks.remove(playerId);
		if (teleportTask == null) {
			return false;
		}

		teleportTask.cancel();
		config.sendMessage(player, config.messageErrorTeleportCancelled());
		return true;
	}

	private Component messageOnTeleport() {
		return config.getMessage("on-teleport", null);
	}
}
