package org.akitiltka.nofallll;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Nofallll extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getEntity();

        if (event.getCause() == DamageCause.VOID) {
            final Location spawnLocation = player.getWorld().getSpawnLocation();
            final World world = spawnLocation.getWorld();

            spawnLocation.add(0, 5, 0);
            player.teleport(spawnLocation);

            new BukkitRunnable() {
                private int ticks = 0;

                @Override
                public void run() {
                    if (player.isOnGround() || ticks >= 100) {
                        cancel();
                    } else {
                        ticks++;
                        player.setFallDistance(ticks / 20f);
                    }
                }
            }.runTaskTimer(this, 1L, 1L);

            event.setCancelled(true);

            player.sendMessage("Вы спасены от падения!");
        }
    }
}
