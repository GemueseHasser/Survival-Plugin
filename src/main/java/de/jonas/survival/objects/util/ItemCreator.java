package de.jonas.survival.objects.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ItemCreator {

    private final Material material;
    private final String name;
    private final List<Enchantment> enchantments;

    public ItemCreator(
        @NotNull final Material material,
        @NotNull final String name,
        @NotNull final List<Enchantment> enchantments
    ) {
        this.material = material;
        this.name = ChatColor.DARK_RED.toString() + ChatColor.BOLD + name;
        this.enchantments = enchantments;
    }

    public ItemStack getStack() {
        final ItemStack stack = new ItemStack(this.material);
        final ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(this.name);
        for (@NotNull final Enchantment enchantment : enchantments) {
            meta.addEnchant(enchantment, 10, true);
        }

        stack.setItemMeta(meta);
        return stack;
    }

}
