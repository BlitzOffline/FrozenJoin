package com.github.frcsty.command

import com.github.frcsty.actions.util.color
import com.github.frcsty.actions.util.replacePlaceholder
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.load.Settings
import java.util.logging.Logger
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("frozenjoin")
@Alias("join", "fjoin")
class HelpCommand(
    private val messageLoader: MessageLoader,
    private val settings: Settings,
    private val logger: Logger,
) : CommandBase() {

    companion object {
        private const val COMMAND: String = "help"
        private const val PERMISSION: String = "join.command.help"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun helpCommand(sender: CommandSender) {
        val help = messageLoader.getMessageList("helpMessage")

        for (line in help) {
            sender.sendMessage(line.replacePlaceholder("{version}", settings.pluginVersion).color(settings.hexPatterns))
        }

        if (settings.debug) logger.info("Executor ${sender.name} executed action 'help'")
    }
}