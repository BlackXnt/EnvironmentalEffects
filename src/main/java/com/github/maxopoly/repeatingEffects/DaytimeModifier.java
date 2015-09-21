package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Allows to change the length of ingame days or to set the time to a specific
 * value permanently. All of this is only done playerside, so the players time
 * will be async with the server time. This plugin also contains listener to
 * adjust the time for a player right away whenever he respawns or logs in
 * 
 * @author Max
 *
 */
public class DaytimeModifier extends RepeatingEffect {

	private final Float daySpeed; // multiplier, 1.0 makes for standard 20 min
									// days, 0.5
	// would be 40 min day,
	// 2.0 10 min etc.
	private long previousRunTime; // in ticks
	private long currentRunTime; // in ticks

	public DaytimeModifier(JavaPlugin plugin, LinkedList<Area> areas, Long startingTime,
			Float daySpeed, long updateTime) {
		super(plugin, areas, updateTime);
		this.currentRunTime = startingTime;
		this.daySpeed = daySpeed;
	}

	public void run() {
		currentPlayers = getCurrentPlayers();
		currentRunTime = previousRunTime + (long) (getUpdateTime() * daySpeed);
		if (currentRunTime > 23999) {
			currentRunTime -= 24000L;
		}
		for (Player p : currentPlayers) {
			applyToPlayer(p);
		}
		previousRunTime = currentRunTime;
		scheduleNextRun();

	}

	/**
	 * If the player is online and in the right area, his time is set to the
	 * desired time
	 */
	public void applyToPlayer(Player p) {
		if (p != null) {
			if (isPlayerInArea(p)) {
				p.setPlayerTime(currentRunTime, false);
			}
		}
	}
}
