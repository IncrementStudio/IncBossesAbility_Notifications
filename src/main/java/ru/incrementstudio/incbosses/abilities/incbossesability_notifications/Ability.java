package ru.incrementstudio.incbosses.abilities.incbossesability_notifications;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.incrementstudio.incapi.utils.ColorUtil;
import ru.incrementstudio.incbosses.api.AbilityBase;
import ru.incrementstudio.incbosses.api.StartReason;
import ru.incrementstudio.incbosses.api.StopReason;
import ru.incrementstudio.incbosses.api.bosses.Boss;
import ru.incrementstudio.incbosses.api.bosses.phases.Phase;

import java.util.ArrayList;
import java.util.List;

public class Ability extends AbilityBase {
    private final List<String>
            startNotification = new ArrayList<>(),
            spawnNotification = new ArrayList<>(),
            stopNotification = new ArrayList<>(),
            deathNotification = new ArrayList<>(),
            notSpawnNotification = new ArrayList<>(),
            notDeathNotification = new ArrayList<>();
    public Ability(Boss boss, Phase phase, FileConfiguration bossConfig, ConfigurationSection abilityConfig) {
        super(boss, phase, bossConfig, abilityConfig);
        if (abilityConfig.contains("start"))
            startNotification.addAll(abilityConfig.getStringList("start"));
        if (abilityConfig.contains("spawn"))
            spawnNotification.addAll(abilityConfig.getStringList("spawn"));
        if (abilityConfig.contains("stop"))
            stopNotification.addAll(abilityConfig.getStringList("stop"));
        if (abilityConfig.contains("death"))
            deathNotification.addAll(abilityConfig.getStringList("death"));
        if (abilityConfig.contains("not-spawn"))
            notSpawnNotification.addAll(abilityConfig.getStringList("not-spawn"));
        if (abilityConfig.contains("not-death"))
            notDeathNotification.addAll(abilityConfig.getStringList("not-death"));
    }

    public void notify(List<String> notification) {
        if (notification.isEmpty()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String line : notification) {
                player.sendMessage(ColorUtil.toColor(line
                        .replace("%boss-name%", boss.getData().getBossName())
                        .replace("%phase-name%", phase.getData().getPhaseName())
                        .replace("%player%", player.getName())
                ));
            }
        }
    }

    @Override
    public void start(StartReason reason) {
        notify(startNotification);
        if (reason == StartReason.SPAWN)
            notify(spawnNotification);
        else if (reason == StartReason.PHASE_CHANGING)
            notify(notSpawnNotification);
    }

    @Override
    public void stop(StopReason reason) {
        notify(stopNotification);
        if (reason == StopReason.DEATH)
            notify(deathNotification);
        else if (reason == StopReason.PHASE_CHANGING)
            notify(notDeathNotification);
    }
}
