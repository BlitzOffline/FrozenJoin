package com.github.frcsty.placeholder

import com.github.frcsty.load.Loader
import java.text.DecimalFormat
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PositionPlaceholder(private val loader: Loader) : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "frozenjoin"
    }

    override fun getVersion(): String {
        return "2.3.2"
    }

    override fun getAuthor(): String {
        return "Frcsty"
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String? {
        when (params) {
            "player-position" -> {
                return loader.positionStorage.positions[player.uniqueId]?.toString() ?: ""
            }
        }

        return null
    }
}