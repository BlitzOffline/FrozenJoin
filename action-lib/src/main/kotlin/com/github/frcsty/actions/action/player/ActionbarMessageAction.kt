package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.util.color
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.entity.Player

object ActionbarMessageAction : Action {
    override val id = "ACTIONBARMESSAGE"

    override fun run(player: Player, data: String, settings: Settings, cache: PlaceholderCache?) {
        player.sendActionBar(data.getTranslatedMessage(player, cache).color(settings.hexPatterns))
    }
}