package se.rsrp.hometeleport.tasks;

import net.kyori.adventure.text.Component;
import se.rsrp.hometeleport.Config;
import se.rsrp.hometeleport.TeleportManager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask extends BukkitRunnable {
	private final Config config;
	private final TeleportManager teleportManager;
	private final Player player;

	int secondsUntilTeleport;

	public TeleportTask(Config config, TeleportManager teleportManager, Player player) {
		this.config = config;
		this.teleportManager = teleportManager;
		this.player = player;
		secondsUntilTeleport = config.secondsToTeleport();
	}

	@Override
	public void run() {
		if (secondsUntilTeleport == 0) {
			teleportManager.finishTeleport(player);
			this.cancel();
		} else {
			boolean showMessage = false;
			if (secondsUntilTeleport >= 10) {
				showMessage = secondsUntilTeleport % 10 == 0;
			} else if (secondsUntilTeleport == 5) {
				showMessage = true;
			} else if (secondsUntilTeleport <= 3) {
				showMessage = true;
			}

			if (showMessage) {
				config.sendMessage(player, messageTeleportingIn(secondsUntilTeleport));
			}

			secondsUntilTeleport--;
		}
	}

	private Component messageTeleportingIn(long timeLeft) {
		return config.getMessage("teleporting-in", message -> message.replace("%seconds%", Long.toString(timeLeft)));
	}
}
