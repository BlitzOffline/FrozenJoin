package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.util.sendPersistentActionbarMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object PersistentActionbarBroadcastAction : Action {
    override val id = "PERSISTENTACTIONBARBROADCAST"

    override fun run(plugin: Plugin, player: Player, data: String, settings: Settings, cache: PlaceholderCache?) {
        sendPersistentActionbarMessage(plugin, data, cache, player, Bukkit.getServer().onlinePlayers, settings.hexPatterns)
    }
}