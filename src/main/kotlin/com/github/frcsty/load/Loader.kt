package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.actions.ActionHandler
import com.github.frcsty.actions.load.Loader
import com.github.frcsty.actions.util.color
import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.command.*
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.listener.base.PlayerJoinListener
import com.github.frcsty.listener.base.PlayerQuitListener
import com.github.frcsty.`object`.FormatManager
import com.github.frcsty.placeholder.PositionPlaceholder
import com.github.frcsty.position.PositionStorage
import me.mattstudios.mf.base.CommandManager
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit.getServer
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener

class Loader(private val plugin: FrozenJoinPlugin) : Loader {

    override val settings = Settings(plugin)
    override val placeholderCache = PlaceholderCache(plugin, settings)
    override val actionHandler = ActionHandler(plugin, this, settings)
    val formatManager = FormatManager(plugin)
    val positionStorage = PositionStorage
    private val messageLoader = MessageLoader(plugin, settings)

    fun initialize() {
        plugin.saveDefaultConfig()
        settings.loadHexPatterns()
        positionStorage.initialize(plugin)
        messageLoader.load()

        if (settings.metrics) {
            val metrics = Metrics(plugin, 6743)
        }

        val manager = CommandManager(plugin)

        registerCompletions(
            manager,
            Pair("#format-argument", listOf("join", "quit")),
            Pair("#format-message", listOf("<message>")),
            Pair("#convert-command", listOf("generate", "start", "dump"))
        )

        manager.register(
            HelpCommand(messageLoader, settings, plugin.logger),
            InfoCommand(messageLoader, settings, plugin.logger),
            MotdCommand(
                loader = this,
                messageLoader = messageLoader,
                plugin = plugin
            ),
            ReloadCommand(plugin, this, messageLoader),
            ConvertCommand(plugin, messageLoader, settings)
        )

        actionHandler.loadDefault()
        manager.register(FormatCommand(messageLoader, this))

        formatManager.setFormats()

        registerMessages(manager, messageLoader)
        registerListeners(PlayerJoinListener(this, plugin), PlayerQuitListener(plugin, this), placeholderCache)
        placeholderCache.runTaskTimerAsynchronously(plugin, 1, settings.cacheUpdateInterval)

        PositionPlaceholder(this).register()
    }

    private fun registerMessages(manager: CommandManager, messages: MessageLoader) {
        val handler = manager.messageHandler
        with(handler) {
            register("cmd.no.console") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("playerOnlyMessage").color(settings.hexPatterns))
            }
            register("cmd.no.permission") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("denyMessage").color(settings.hexPatterns))
            }
            register("cmd.no.exists") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("unknownCommandMessage").color(settings.hexPatterns))
            }
            register("cmd.wrong.usage") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("usageMessage").color(settings.hexPatterns))
            }
        }
    }

    private fun registerCompletions(manager: CommandManager, vararg completions: Pair<String, List<String>>) {
        completions.forEach { pair ->
            manager.completionHandler.register(pair.first) { pair.second }
        }
    }

    private fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { listener: Listener? ->
            if (listener != null)
                getServer().pluginManager.registerEvents(listener, plugin)
        }
    }

    fun terminate() {
        plugin.reloadConfig()
        positionStorage.terminate(plugin)
        if (!placeholderCache.isCancelled) placeholderCache.cancel()
    }
}