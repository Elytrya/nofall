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
        // Регистрируем обработчик событий
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

        // Проверяем, является ли причина урона падением
        if (event.getCause() == DamageCause.VOID) {
            // Получаем местоположение спавна игрока
            final Location spawnLocation = player.getWorld().getSpawnLocation();
            final World world = spawnLocation.getWorld();

            // Телепортируем игрока на 5 блоков выше спавна
            spawnLocation.add(0, 5, 0);
            player.teleport(spawnLocation);

            // Устанавливаем плавное падение на 5 секунд
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

            // Отменяем событие урона
            event.setCancelled(true);

            // Отправляем сообщение игроку
            player.sendMessage("Вы спасены от падения!");
        }
    }
}
