package com.emptyirony.cookieaddon;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/11 13:57
 * 4
 */
public class Cooldown
{
    private static final Map<UUID, Integer> cooldowns = new HashMap();

    public static final int DEFAULT_COOLDOWN = 0;


    public static void startCooldownTask(final UUID player) { (new BukkitRunnable()
    {
        public void run() {
            int timeLeft = Cooldown.getCooldown(player);
            Cooldown.setCooldown(player, --timeLeft);
            if (timeLeft == 0) {
                cancel();
            }
        }
    }).runTaskTimerAsynchronously(CookieAddon.getIns(), 20L, 20L); }


    public static void setCooldown(UUID player, int time) {
        if (time < 1) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }


    public static int getCooldown(UUID player) { return cooldowns.getOrDefault(player, 0); }
}
