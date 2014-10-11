package uk.submergedcode.spigotdatabasemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import uk.submergedcode.spigotdatabasemanager.database.IDatabase;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SpigotDatabaseManager {

    /**
     * The singleton instance of the database manager
     */
    private static final SpigotDatabaseManager s_instance;
    
    /**
     * All databases which have been created.
     */
    private final List<IDatabase> m_databases = new ArrayList<IDatabase>();
    
    /**
     * The java plugin
     */
    private SpigotDatabaseManagerPlugin m_plugin = null;
    
    /**
     * Static constructor
     */
    static {
        s_instance = new SpigotDatabaseManager();
    }
    
    /**
     * Access to the singleton instance
     * @return The database manager instance.
     */
    public static SpigotDatabaseManager getInstance() {
        return s_instance;
    }
    
    /**
     * Get the plugin that contains the database manager
     * @return The java plugin
     */
    public SpigotDatabaseManagerPlugin getPlugin() {
        return m_plugin;
    }
    
    /**
     * Set the plugin that contains the database manager
     * @param plugin The SpigotDatabaseManagerPlugin
     */
    public void setPlugin(SpigotDatabaseManagerPlugin plugin) {
        m_plugin = plugin;
    }
    
    /**
     * Called when the plugin is disabled.
     */
    public void onDisable() {

        for (IDatabase database : m_databases) {
            database.shutdown();
        }
        m_databases.clear();
        
    }
        
    /**
     * Create a new database of a given type to be used by a plugin.
     * @param <T>
     * @param owner
     * @param databaseType
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public static <T extends IDatabase> T createDatabase(JavaPlugin owner, Class<T> databaseType) throws InstantiationException, IllegalAccessException {
                
        // Create the new database
        T database = (T) databaseType.newInstance();
        
        // Set the owner and start the update thread
        database.setOwner(owner);
        database.start();
        
        // Store the database
        s_instance.m_databases.add(database);
        
        // Return the fully setup database
        return database;
    }
    
    /**
     * Log an exception to the console
     * @param message The user friendly context of the exception
     */
    public static synchronized void log(String message) {
        getInstance().getPlugin().getLogger().log(Level.INFO, message);
    }
    
    /**
     * Log an exception to the console
     * @param message The user friendly context of the exception
     * @param ex The exception which occurred
     */
    public static synchronized void logException(String message, Exception ex) {
        getInstance().getPlugin().getLogger().log(Level.SEVERE, message, ex);
    }
    
}
