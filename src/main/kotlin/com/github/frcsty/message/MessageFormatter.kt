package com.github.frcsty.message

import com.github.frcsty.actions.ActionHandler
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.time.parseTime
import com.github.frcsty.actions.util.sendTranslatedMessage
import com.github.frcsty.load.Settings
import com.github.frcsty.`object`.FormatManager
import com.github.frcsty.util.getCustomMessage
import java.util.concurrent.TimeUnit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object MessageFormatter {

    fun executeMotd(
        player: Player,
        manager: FormatManager,
        actionHandler: ActionHandler,
        settings: Settings,
        command: Boolean,
        message: String,
        plugin: Plugin,
        cache: PlaceholderCache?,
    ) {
        //This is not the most readable thing in the world, but I'm kind of scared to change anything since there's no test suite
        val motds = manager.motdsMap.filter { (key, value) ->
            !("firstJoin".equals(key, true)) && player.hasPermission(value.permission)
        }.mapKeys {
            it.value.priority
        }.toSortedMap(Comparator.reverseOrder<Int>())

        val motd = motds.entries.firstOrNull()
        if (motd == null) {
            if (command) {
                if (settings.debug) plugin.logger.info("Executor ${player.name} executed action 'motd'")
                player.sendTranslatedMessage(message, settings.hexPatterns, cache)
            }
            return
        }
        val motdObject = motd.value
        val actions: List<String> = motdObject.message
        val delay = motdObject.delay.parseTime().to(TimeUnit.SECONDS) * 20L

        if (!command || (command && motdObject.delayOnCommand)) {
            Bukkit.getScheduler().runTaskLater(
                plugin,
                Runnable {
                    actionHandler.execute(player, actions)
                    if (settings.debug) plugin.logger.info("Executing '${motdObject.identifier}' motd for user ${player.name} (${player.uniqueId})")
                },
                delay
            )
            return
        }

        actionHandler.execute(player, actions)
        if (settings.debug) plugin.logger.info("Executing '${motdObject.identifier}' motd for user ${player.name} (${player.uniqueId})")
    }

    fun executeFormat(
        player: Player,
        manager: FormatManager,
        actionHandler: ActionHandler,
        settings: Settings,
        action: String,
        plugin: Plugin,
    ): List<String> {
        val customMessage = player.getCustomMessage(action)
        if (customMessage != "emptyValue") {
            actionHandler.execute(player, listOf("[BROADCAST] $customMessage"))
            if (settings.debug) plugin.logger.info("Executing custom message for user ${player.name} (${player.uniqueId})")
            return listOf(customMessage)
        }

        val formats = manager.formatsMap.filter { (_, value) ->
            player.hasPermission(value.permission)
        }.map {
            it.value.priority to it.key
        }.toMap().toSortedMap(Comparator.reverseOrder())

        val format = formats.entries.firstOrNull() ?: return emptyList()
        val formatObject = manager.formatsMap[format.value] ?: return emptyList()

        val actions = if (action.equals("join", ignoreCase = true)) {
            formatObject.joinActions
        } else {
            formatObject.leaveActions
        }

        val type = formatObject.type
        if (actions.isEmpty()) {
            return emptyList()
        }

        when (type.uppercase()) {
            "NORMAL" -> {
                actionHandler.execute(player, actions)
            }

            "RANDOM" -> {
                actionHandler.execute(player, listOf(actions.random()))
            }

            "VANISH" -> {
                val inverted = formatObject.isInverted
                if (isVanished(player, inverted)) {
                    actionHandler.execute(player, actions)
                }
            }
        }

        if (settings.debug)
            plugin.logger.info("Executing '${formatObject.identifier}' format for user ${player.name} (${player.uniqueId})")

        return actions
    }

    private fun isVanished(player: Player, inverted: Boolean): Boolean {
        val isVanish = player.getMetadata("vanished").any { it.asBoolean() }
        return !inverted == isVanish
    }
}
