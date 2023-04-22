package com.github.frcsty.actions.util

import com.github.frcsty.actions.cache.PlaceholderCache
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.regex.Pattern

fun getPersistentActionbarMessageAndTime(data: String): Pair<Long, String> {
    val split = data.split(";", limit = 2)
    if (split.size == 1) return Pair(1, data)
    val time = split[0].trim().toLongOrNull() ?: return Pair(1, data)
    if (time <= 3) return Pair(1, split[1])
    return Pair(time, split[1])
}

fun sendPersistentActionbarMessage(
    plugin: Plugin,
    data: String,
    cache: PlaceholderCache?,
    actionPlayer: Player,
    recipientPlayers: Collection<Player>,
    hexPatterns: LinkedHashSet<Pattern>
) {
    val (time, message) = getPersistentActionbarMessageAndTime(data)

    var ran = 0L
    object : BukkitRunnable() {
        override fun run() {
            if (ran++ >= time) {
                cancel()
                return
            }

            for (recipientPlayer in recipientPlayers) {
                recipientPlayer.sendActionBar(message.getTranslatedMessage(actionPlayer, cache).color(hexPatterns))
            }

        }
    }.runTaskTimerAsynchronously(plugin, 20L, 20L)
}