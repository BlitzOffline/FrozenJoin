package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.actions.load.Settings
import java.util.regex.Pattern

private val OLD_HEX_PATTERN: Pattern = Pattern.compile("#<(?<hex>[A-Fa-f0-9]{6})>")
private val AMPERSAND_HEX_PATTERN: Pattern = Pattern.compile("&#(?<hex>[A-Fa-f0-9]{6})")
private val SIMPLE_HEX_PATTERN: Pattern = Pattern.compile("#(?<hex>[A-Fa-f0-9]{6})")

class Settings(private val plugin: FrozenJoinPlugin) : Settings {

    override var hexPatterns = linkedSetOf<Pattern>()
    override val cacheUpdateInterval = plugin.config.getLong("placeholder-cache.update-interval", 20L)
    override var cachedPlaceholders: List<String> = plugin.config.getStringList("placeholder-cache.placeholders")
    override val debug = plugin.config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    override val metrics = plugin.config.getBoolean("stonks", true)

    override val logger = plugin.logger
    override val pluginVersion = plugin.description.version

    fun reload() {
        cachedPlaceholders = plugin.config.getStringList("placeholder-cache.placeholders")
        loadHexPatterns()
    }

    fun loadHexPatterns() {
        hexPatterns.clear()
        if (plugin.config.getBoolean("hex-patterns.old", true)) hexPatterns.add(OLD_HEX_PATTERN)
        if (plugin.config.getBoolean("hex-patterns.ampersand", true)) hexPatterns.add(AMPERSAND_HEX_PATTERN)
        if (plugin.config.getBoolean("hex-patterns.simple", true)) hexPatterns.add(SIMPLE_HEX_PATTERN)
    }
}