package dev.failures.main.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiUtil {
    public static GuiItem createItem(ItemStack item, int quantity, List<String> lore, String name) {
        return ItemBuilder.from(item)
                .name(Component.text(ChatUtil.colorize(name)))
                .lore(ChatUtil.getListComponent(lore))
                .amount(quantity)
                .asGuiItem();
    }

    public static GuiItem createSkull(String texture, int quantity, List<String> lore, String name) {
        return ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize(name)))
                .lore(ChatUtil.getListComponent(lore))
                .amount(quantity)
                .texture(texture)
                .asGuiItem();
    }
}
