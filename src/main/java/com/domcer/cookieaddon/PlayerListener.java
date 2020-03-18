package com.domcer.cookieaddon;


import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Stairs;
import org.bukkit.scheduler.BukkitRunnable;
import strafe.games.core.util.CC;
import strafe.games.core.util.ItemBuilder;
import strafe.games.core.util.LocationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/11 13:55
 * 4
 */
public class PlayerListener implements Listener {
    public static Map<UUID, Hologram> cache = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(CC.translate("&6&o➣ &e欢迎来到街机游戏"));
        e.getPlayer().sendMessage(CC.translate("&6&o➣ &e挑选一个座位坐下. 即可开始游戏!"));
        e.getPlayer().getInventory().clear();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (e.getPlayer().getVehicle() != null) {
            return;
        }
        if (Cooldown.getCooldown(e.getPlayer().getUniqueId()) > 1) {
            e.getPlayer().sendMessage(CC.translate("&4&o➣ &c请不要重复尝试坐下!"));
            return;
        }
        if (e.getItem() != null){
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6右键回到街机游戏"))){
                Bukkit.dispatchCommand(e.getPlayer(),"gb");
                return;
            }
        }


        if (e.getClickedBlock().getType() == Material.QUARTZ_STAIRS) {
            if (CookieAddon.getSitBlocks().containsValue(e.getClickedBlock())) {
                e.getPlayer().sendMessage(CC.translate("&4&o➣ &c这个座位已经有人了!换一个座位吧~"));
                return;
            }
            CookieAddon.getSitBlocks().put(e.getPlayer().getName(), e.getClickedBlock());
            CookieAddon.siting.add(e.getPlayer().getUniqueId());

            Location location = getSitLocation(e.getClickedBlock());
            Block block = e.getClickedBlock();
            Stairs stairs = (Stairs) block.getState().getData();
            ArmorStand stand = (ArmorStand) e.getPlayer().getWorld().spawnEntity(location.add(0, 0.2, 0), EntityType.ARMOR_STAND);
            Bukkit.getScheduler().runTask(CookieAddon.getIns(), () -> {
                if (stand.isValid()) {
                    stand.setVisible(false);
                    stand.setGravity(false);
                    stand.setMarker(true);
                    stand.setCustomName("椅子");
                    stand.setCustomNameVisible(false);
                    Location loc = e.getPlayer().getLocation();
                    loc.setYaw(Util.faceToYaw(stairs.getDescendingDirection()));
                    loc.setPitch(0);
                    e.getPlayer().teleport(loc);
                    stand.setPassenger(e.getPlayer());
                    CookieAddon.getChars().put(e.getPlayer().getName(), stand);
                }
                Cooldown.setCooldown(e.getPlayer().getUniqueId(), 5);
                Cooldown.startCooldownTask(e.getPlayer().getUniqueId());

                Bukkit.dispatchCommand(e.getPlayer(), "gb");
            });
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (CookieAddon.getChars().containsKey(e.getPlayer().getName()) &&
                e.getPlayer().getVehicle() == null) {
            ArmorStand removeStand = CookieAddon.getChars().remove(e.getPlayer().getName());
            if (removeStand != null) {
                removeStand.remove();
                e.getPlayer().updateInventory();
                CookieAddon.getSitBlocks().remove(e.getPlayer().getName());
                CookieAddon.siting.remove(e.getPlayer().getUniqueId());
                e.getPlayer().getInventory().remove(Material.CHEST);
            }

            if (e.getPlayer().getInventory().contains(Material.CHEST)){
                e.getPlayer().getInventory().remove(Material.CHEST);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (CookieAddon.getChars().containsKey(e.getPlayer().getName()) &&
                e.getPlayer().getVehicle() == null) {
            ArmorStand removeStand = CookieAddon.getChars().remove(e.getPlayer().getName());
            if (removeStand != null) {
                removeStand.remove();
            }

            Hologram hologram = cache.get(e.getPlayer().getUniqueId());
            if (hologram != null) {
                hologram.delete();
            }
        }

        CookieAddon.getSitBlocks().remove(e.getPlayer().getName());
        CookieAddon.siting.remove(e.getPlayer().getUniqueId());
    }

    private Location getSitLocation(Block block) {
        double sh = 0.7D;
        Stairs stairs = null;
        if (block.getState().getData() instanceof Stairs) {
            stairs = (Stairs) block.getState().getData();
        }
        Location plocation = block.getLocation();
        plocation.add(0.5D, sh - 0.5D, 0.5D);
        switch (stairs.getDescendingDirection()) {
            case NORTH:
                plocation.setYaw(180.0F);
                break;

            case EAST:
                plocation.setYaw(-90.0F);
                break;

            case SOUTH:
                plocation.setYaw(0.0F);
                break;

            case WEST:
                plocation.setYaw(90.0F);
                break;
        }

        return plocation;
    }


    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        if (cache.get(event.getPlayer().getUniqueId()) != null){
            ItemStack itemStack = new ItemBuilder(Material.CHEST)
                    .shiny()
                    .name("&6右键回到街机游戏")
                    .build();

            event.getPlayer().getInventory().setItem(4,itemStack);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldInitEvent event) {
        event.getWorld().getEntities().forEach(entity -> {
            if (entity instanceof ArmorStand) {
                entity.remove();
            }
        });
    }
}
