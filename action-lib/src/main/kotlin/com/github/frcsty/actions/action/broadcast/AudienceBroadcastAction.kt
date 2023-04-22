package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.util.sendTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AudienceBroadcastAction : Action {
    override val id = "AUDIENCEBROADCAST"

    override fun run(player: Player, data: String, settings: Settings, cache: PlaceholderCache?) = Bukkit.getServer().onlinePlayers.forEach {
        if (player == it) return@forEach
        it.sendTranslatedMessage(data, player, settings.hexPatterns, cache)
    }
}