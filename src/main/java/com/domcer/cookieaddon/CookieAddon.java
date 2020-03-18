package com.domcer.cookieaddon;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import strafe.games.core.util.board.assemble.Assemble;

import java.util.*;

public final class CookieAddon extends JavaPlugin {
    public static CookieAddon ins;

    public static Map<String, Block> getSitBlocks() { return sitBlocks; }
    private static Map<String, Block> sitBlocks = new HashMap<>();
    public static Map<String, ArmorStand> getChars() { return chars; }
    private static Map<String, ArmorStand> chars = new HashMap<>();
    public static List<UUID> siting = new ArrayList<>();
    public static CookieAddon getIns() { return ins; }

    @Override
    public void onEnable() {
        // Plugin startup logic
        ins = this;
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new Assemble(this,new ScoreImpl());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                    if (entity instanceof ArmorStand){
                        if (!((ArmorStand) entity).isVisible()) {
                            if (entity.getPassenger() == null) {
                                entity.remove();
                            }
                        }
                    }
                }

                PlayerListener.cache.forEach((uuid, hologram) -> {
                    if (Bukkit.getPlayer(uuid) == null){
                        hologram.delete();
                    }
                });
            }
        }.runTaskTimer(this,20,20*60);
    }
}
