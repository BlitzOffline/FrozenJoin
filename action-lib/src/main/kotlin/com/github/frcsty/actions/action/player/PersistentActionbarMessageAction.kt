package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.util.sendPersistentActionbarMessage
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object PersistentActionbarMessageAction : Action {
    override val id = "PERSISTENTACTIONBARMESSAGE"

    override fun run(plugin: Plugin, player: Player, data: String, settings: Settings, cache: PlaceholderCache?) {
        sendPersistentActionbarMessage(plugin, data, cache, player, listOf(player), settings.hexPatterns)
    }
}