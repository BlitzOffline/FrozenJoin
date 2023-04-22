package com.github.frcsty.command

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.actions.util.color
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.load.Loader
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable

@Command("frozenjoin")
@Alias("join", "fjoin")
class ReloadCommand(
    private val plugin: FrozenJoinPlugin,
    private val loader: Loader,
    private val messageLoader: MessageLoader,
) : CommandBase() {

    companion object {
        private const val COMMAND: String = "reload"
        private const val PERMISSION: String = "join.command.base"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun reloadCommand(sender: CommandSender) {
        val startTime = System.currentTimeMillis()
        val message = messageLoader.getMessage("reloadMessage")

        loader.formatManager.formatsMap.clear()
        loader.formatManager.motdsMap.clear()

        object : BukkitRunnable() {
            override fun run() {
                plugin.reloadConfig()
                loader.formatManager.setFormats()
                messageLoader.load()
                loader.settings.reload()
            }
        }.runTaskAsynchronously(plugin)

        val estimatedTime = System.currentTimeMillis() - startTime
        sender.sendMessage((message.replace("%time%", estimatedTime.toString()).color(loader.settings.hexPatterns)))

        if (loader.settings.debug) plugin.logger.info("Executor ${sender.name} executed action 'reload'")
    }
}