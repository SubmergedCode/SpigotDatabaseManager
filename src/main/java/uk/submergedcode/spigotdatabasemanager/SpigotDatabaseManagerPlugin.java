package uk.submergedcode.spigotdatabasemanager;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SpigotDatabaseManagerPlugin extends JavaPlugin {
        
    /**
     * Called when the plugin is loaded.
     */
    @Override
    public void onLoad() {
        
        SpigotDatabaseManager.getInstance().setPlugin(this);
        
    }
    
    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        
        SpigotDatabaseManager.getInstance().onDisable();
        
    }
    
}
