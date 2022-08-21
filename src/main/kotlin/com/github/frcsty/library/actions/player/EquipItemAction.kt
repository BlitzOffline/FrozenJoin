package com.github.frcsty.library.actions.player

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.color
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object EquipItemAction : Action {

    override val id = "EQUIP"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        val args = data.split(";")
        val vars: List<String> = args[0].split(",")
        val useData = vars.size == 2
        val material: Material =
                if (useData) Material.getMaterial(vars[1]) ?: return
                else Material.getMaterial(args[0]) ?: return
        val damage = if (useData) Integer.valueOf(vars[1]) else 0
        val amount = args[3].toInt()
        val display = args[1]
        val slot = args[4].toInt()
        val lore = args[2].split("~")

        val item: ItemStack = if (useData) {
            ItemStack(material, amount, damage.toShort())
        } else {
            ItemStack(material, amount)
        }

        val meta = item.itemMeta
        meta.setDisplayName(display.getTranslatedMessage(player, cache).color())
        if (lore.isNotEmpty()) {
            meta.lore = lore.map { it.getTranslatedMessage(player, cache).color() }
        }

        item.itemMeta = meta

        if (player.inventory.getItem(slot) == null) {
            player.inventory.setItem(slot, item)
        } else {
            player.inventory.addItem(item)
        }
    }
}