package se.rsrp.hometeleport.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import se.rsrp.hometeleport.TeleportManager;

public class PlayerLeave implements Listener {
	private final TeleportManager teleportManager;

	public PlayerLeave(TeleportManager teleportManager) {
		this.teleportManager = teleportManager;
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		teleportManager.cancelTeleport(event.getPlayer());
	}
}
