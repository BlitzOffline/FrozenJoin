package com.github.frcsty.actions.util

import com.github.frcsty.actions.cache.PlaceholderCache
import java.util.regex.Pattern
import me.clip.placeholderapi.PlaceholderAPI
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

fun String.color(hexPatterns: LinkedHashSet<Pattern>): String {
    var translation = this
    for (hexPattern in hexPatterns) {
        var matcher = hexPattern.matcher(translation)

        while (matcher.find()) {
            val hex: ChatColor = ChatColor.of("#" + matcher.group("hex"))
            val before = translation.substring(0, matcher.start())
            val after = translation.substring(matcher.end())
            translation = before + hex + after
            matcher = hexPattern.matcher(translation)
        }
    }

    return ChatColor.translateAlternateColorCodes('&', translation)
}

fun String.setPAPIPlaceholders(player: Player, cache: PlaceholderCache? = null): String {
    return PlaceholderAPI.setPlaceholders(player, cache?.setPlaceholders(this, player) ?: this)
}

fun String.replacePlaceholder(placeholder: String, value: String): String {
    return this.replace(placeholder, value)
}

fun Player.sendTranslatedMessage(message: String, hexPatterns: LinkedHashSet<Pattern>, cache: PlaceholderCache? = null) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(this, cache).color(hexPatterns)))
}

fun Player.sendTranslatedMessage(message: String, player: Player, hexPatterns: LinkedHashSet<Pattern>, cache: PlaceholderCache? = null) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(player, cache).color(hexPatterns)))
}

fun CommandSender.sendTranslatedMessage(player: Player, message: String, hexPatterns: LinkedHashSet<Pattern>, cache: PlaceholderCache? = null) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(player, cache).color(hexPatterns)))
}

fun String.getTranslatedMessage(player: Player, cache: PlaceholderCache? = null): String {
    var message = this
    val daddy: Plugin? = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")

    if (daddy != null && daddy.isEnabled) {
        message = message.setPAPIPlaceholders(player, cache)
    }

    return message
}