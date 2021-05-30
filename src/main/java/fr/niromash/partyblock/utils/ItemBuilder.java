package fr.niromash.partyblock.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack is;

    public ItemBuilder(final Material mat) {
        this.is = new ItemStack(mat);
    }

    public ItemBuilder(final ItemStack is) {
        this.is = is;
    }

    public ItemBuilder amount(final int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(final String name) {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final String name) {
        final ItemMeta meta = this.is.getItemMeta();
        List<String> lore = (List<String>)meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.add(name);
        meta.setLore((List)lore);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(){
        final ItemMeta meta = this.is.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final List<String> lore) {
        final List<String> toSet = new ArrayList<String>();
        final ItemMeta meta = this.is.getItemMeta();
        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        meta.setLore((List)toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(final int durability) {
        this.is.setDurability((short)durability);
        return this;
    }

    public ItemBuilder data(final int data) {
        this.is.setData(new MaterialData(this.is.getType(), (byte)data));
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment) {
        this.is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(final Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setLore((List)new ArrayList());
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (final Enchantment e : this.is.getEnchantments().keySet()) {
            this.is.removeEnchantment(e);
        }
        return this;
    }

    public ItemBuilder color(final Color color) {
        if (this.is.getType() == Material.LEATHER_BOOTS || this.is.getType() == Material.LEATHER_CHESTPLATE || this.is.getType() == Material.LEATHER_HELMET || this.is.getType() == Material.LEATHER_LEGGINGS) {
            final LeatherArmorMeta meta = (LeatherArmorMeta)this.is.getItemMeta();
            meta.setColor(color);
            this.is.setItemMeta((ItemMeta)meta);
            return this;
        }
        throw new IllegalArgumentException("The color method is only applicable for leather armor");
    }

    public ItemStack build() {
        return this.is;
    }

    public static ItemStack createGoldenHead() {
        return createGoldenHead(1);
    }

    public static ItemStack createGoldenHead(int amount) {
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, amount);
        ItemMeta appleMeta = apple.getItemMeta();
        appleMeta.setDisplayName(ChatColor.GOLD + "Golden Head");
        apple.setItemMeta(appleMeta);
        return apple;
    }

    // Todo not working
//    public static ItemStack getHead(Player player) {
//        ItemStack item = new ItemStack(Material.SKELETON_SKULL, 1, (short) 3);
//        SkullMeta skull = (SkullMeta) item.getItemMeta();
//        skull.setOwner(player.getName());
//        item.setItemMeta(skull);
//        return item;
//    }
}
