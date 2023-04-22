package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.util.sendTranslatedMessage
import org.bukkit.entity.Player

object MessageAction : Action {
    override val id = "MESSAGE"

    override fun run(player: Player, data: String, settings: Settings, cache: PlaceholderCache?) =
        player.sendTranslatedMessage(data, player, settings.hexPatterns, cache)
}