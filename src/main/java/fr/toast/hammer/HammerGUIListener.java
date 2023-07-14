package fr.toast.hammer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener for the Hammer GUI (Graphical User Interface) events.
 */
public class HammerGUIListener implements Listener {
    private final JavaPlugin plugin;
    private final String repairSuccessMessage;
    private final String repairNotOpMessage;
    private final String statusUnlocked;
    private final String statusLocked;

    /**
     * Constructor for the HammerGUIListener class.
     *
     * @param plugin The associated JavaPlugin instance.
     */
    public HammerGUIListener(JavaPlugin plugin) {
        this.plugin = plugin;
        // Load configuration values for messages
        this.repairSuccessMessage = plugin.getConfig().getString("repairSuccessMessage", "&aYour pickaxe has been repaired!");
        this.repairNotOpMessage = plugin.getConfig().getString("repairNotOpMessage", "&cYou must be an operator to use the Repair enchantment!");
        this.statusUnlocked = plugin.getConfig().getString("statusUnlocked", "&aSTATUS: Unlocked");
        this.statusLocked = plugin.getConfig().getString("statusLocked", "&cSTATUS: Locked (OP only)");
    }

    /**
     * Method called when a player right-clicks while sneaking.
     *
     * @param event The PlayerInteractEvent triggered by the player's action.
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        // Check if the player is holding a diamond pickaxe, sneaking, and right-clicking
        if (itemInHand.getType() == Material.DIAMOND_PICKAXE && player.isSneaking() && event.getAction().name().contains("RIGHT_CLICK")) {
            // Create a GUI inventory for tool enchantments
            Inventory gui = plugin.getServer().createInventory((InventoryHolder) null, 9, "Enchant your tool");

            // Add Hammer enchantment option
            ItemStack hammerEnchant = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta hammerEnchantMeta = hammerEnchant.getItemMeta();
            hammerEnchantMeta.setDisplayName("Hammer");
            hammerEnchant.setItemMeta(hammerEnchantMeta);
            gui.setItem(0, hammerEnchant);

            // Add Repair enchantment option
            ItemStack repairEnchant = new ItemStack(Material.ANVIL);
            ItemMeta repairEnchantMeta = repairEnchant.getItemMeta();
            repairEnchantMeta.setDisplayName("Repair");
            // Set lore based on player's operator status
            if (player.isOp()) {
                repairEnchantMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', statusUnlocked)));
            } else {
                repairEnchantMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', statusLocked)));
            }
            repairEnchant.setItemMeta(repairEnchantMeta);
            gui.setItem(1, repairEnchant);

            // Open the GUI for the player
            player.openInventory(gui);
        }
    }

    /**
     * Method called when a player clicks in an inventory.
     *
     * @param event The InventoryClickEvent triggered by the player's click.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the tool enchantment GUI
        if (event.getView().getTitle().equals("Enchant your tool")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            // Handle the clicked item based on its display name
            if (clickedItem.getItemMeta().getDisplayName().equals("Hammer")) {
                // Add the Hammer enchantment lore to the player's tool
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                ItemMeta meta = itemInHand.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add(ChatColor.GOLD + "Enchantment: Hammer");
                meta.setLore(lore);
                itemInHand.setItemMeta(meta);
                // Close the inventory for the player
                player.closeInventory();
            } else if (clickedItem.getItemMeta().getDisplayName().equals("Repair")) {
                // Handle repair option based on player's operator status
                if (player.isOp()) {
                    // Fully repair the player's tool
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    ItemMeta meta = itemInHand.getItemMeta();
                    if (meta instanceof Damageable) {
                        Damageable damageable = (Damageable) meta;
                        damageable.setDamage(0); // fully repair
                        itemInHand.setItemMeta(meta);
                        // Send repair success message to the player
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', repairSuccessMessage));
                    }
                    // Close the inventory for the player
                    player.closeInventory();
                } else {
                    // Send repair not operator message to the player
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', repairNotOpMessage));
                }
            }
        }
    }
}
