package com.github.frcsty.actions.action

import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

interface Action {
    val id: String
    fun run(player: Player, data: String, cache: PlaceholderCache? = null) {}
    fun run(player: Player, data: String, settings: Settings, cache: PlaceholderCache? = null) {}
    fun run(plugin: Plugin, player: Player, data: String, cache: PlaceholderCache? = null) {}
    fun run(plugin: Plugin, player: Player, data: String, settings: Settings, cache: PlaceholderCache? = null) {}
}