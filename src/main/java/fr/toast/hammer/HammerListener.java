package fr.toast.hammer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for the Hammer functionality.
 */
public class HammerListener implements Listener {
    private static final int RANGE = 1;
    private final List<String> blacklistedBlocks;
    private final boolean loseDurability;
    private final int warningDurability;
    private final String warningTitle;
    private final String warningSubtitle;
    private final Logger logger;
    private final boolean loggingEnabled;

    /**
     * Constructor for the HammerListener class.
     *
     * @param plugin The associated JavaPlugin instance.
     */
    public HammerListener(JavaPlugin plugin) {
        // Load configuration values
        this.blacklistedBlocks = plugin.getConfig().getStringList("blacklistedBlocks");
        this.loseDurability = plugin.getConfig().getBoolean("loseDurability", true);
        this.warningDurability = plugin.getConfig().getInt("warningDurability", 100);
        this.warningTitle = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("warningTitle", "&cAttention"));
        this.warningSubtitle = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("warningSubtitle", "&eVotre pioche est presque cassée!"));
        this.loggingEnabled = plugin.getConfig().getBoolean("loggingEnabled", false);
        this.logger = plugin.getLogger();
    }

    /**
     * Method called when a block is broken by a player.
     *
     * @param event The BlockBreakEvent triggered by the block break.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();
        // Check if the player is holding a diamond pickaxe with the Hammer enchantment
        if (itemInHand.getType() == Material.DIAMOND_PICKAXE) {
            if (!hasHammerEnchant(itemInHand)) {
                return;
            }
            Block block = event.getBlock();
            // Check if the broken block is blacklisted
            if (blacklistedBlocks.contains(block.getType().name())) {
                logInfo("A block break attempt was blocked.");
                return;
            }
            breakSurroundingBlocks(block, itemInHand, event.getPlayer());
        }
    }

    /**
     * Checks if the given item has the Hammer enchantment.
     *
     * @param item The ItemStack to check.
     * @return True if the item has the Hammer enchantment, false otherwise.
     */
    private boolean hasHammerEnchant(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            return item.getItemMeta().getLore().contains(ChatColor.GOLD + "Enchantement : Hammer");
        }
        return false;
    }

    /**
     * Breaks the blocks in the surrounding area of the given center block.
     *
     * @param centerBlock The center block.
     * @param tool        The ItemStack representing the player's tool.
     * @param player      The player who broke the block.
     */
    private void breakSurroundingBlocks(Block centerBlock, ItemStack tool, Player player) {
        int blockX = centerBlock.getX();
        int blockY = centerBlock.getY();
        int blockZ = centerBlock.getZ();

        for (int x = blockX - RANGE; x <= blockX + RANGE; x++) {
            for (int y = blockY - RANGE; y <= blockY + RANGE; y++) {
                for (int z = blockZ - RANGE; z <= blockZ + RANGE; z++) {
                    Block targetBlock = centerBlock.getWorld().getBlockAt(x, y, z);
                    // Break the target block if it's not blacklisted
                    if (!blacklistedBlocks.contains(targetBlock.getType().name())) {
                        targetBlock.breakNaturally(tool);
                        decreaseDurability(tool, player);
                    }
                }
            }
        }
    }

    /**
     * Decreases the durability of the tool after breaking a block.
     *
     * @param tool   The ItemStack representing the player's tool.
     * @param player The player who used the tool.
     */
    private void decreaseDurability(ItemStack tool, Player player) {
        if (loseDurability) {
            ItemMeta meta = tool.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                int newDurability = damageable.getDamage() + 1;
                if (newDurability >= tool.getType().getMaxDurability()) {
                    // Break the tool if it reaches maximum durability
                    tool.setAmount(0);
                    logInfo("A pickaxe has been broken.");
                } else {
                    damageable.setDamage(newDurability);
                    tool.setItemMeta(meta);
                    // Send warning message if durability is low
                    if (newDurability >= tool.getType().getMaxDurability() - warningDurability) {
                        player.sendTitle(warningTitle, warningSubtitle, 10, 70, 20);
                    }
                }
            }
        }
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    private void logInfo(String message) {
        if (loggingEnabled) {
            logger.info(message);
        }
    }
}
