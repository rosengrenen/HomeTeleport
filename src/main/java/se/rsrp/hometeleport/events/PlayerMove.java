package se.rsrp.hometeleport.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import se.rsrp.hometeleport.TeleportManager;

public class PlayerMove implements Listener {
	private final TeleportManager teleportManager;

	public PlayerMove(TeleportManager teleportManager) {
		this.teleportManager = teleportManager;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!teleportManager.isTeleporting(player)) {
			return;
		}

		boolean movedX = event.getFrom().getBlockX() != event.getTo().getBlockX();
		boolean movedY = event.getFrom().getBlockY() != event.getTo().getBlockY();
		boolean movedZ = event.getFrom().getBlockZ() != event.getTo().getBlockZ();
		boolean moved = movedX || movedY || movedZ;
		if (moved) {
			teleportManager.cancelTeleport(player);
		}
	}
}
