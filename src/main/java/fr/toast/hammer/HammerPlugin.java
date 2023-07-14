package fr.toast.hammer;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class for the Hammer plugin.
 */
public class HammerPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Save the default configuration file if it doesn't exist
        this.saveDefaultConfig();

        // Register the HammerListener and HammerGUIListener as event listeners
        getServer().getPluginManager().registerEvents(new HammerListener(this), this);
        getServer().getPluginManager().registerEvents(new HammerGUIListener(this), this);
    }
}
