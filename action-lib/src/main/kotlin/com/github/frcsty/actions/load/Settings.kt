package com.github.frcsty.actions.load

import java.util.logging.Logger
import java.util.regex.Pattern

interface Settings {
    val hexPatterns: LinkedHashSet<Pattern>
    val cacheUpdateInterval: Long
    var cachedPlaceholders: List<String>
    val debug: Boolean
    val metrics: Boolean

    val logger: Logger
    val pluginVersion: String
}