/**
 * Copyright (c) 2014 SubmergedCode
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uk.submergedcode.spigotdatabasemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import uk.submergedcode.spigotdatabasemanager.database.IDatabase;
import uk.submergedcode.spigotdatabasemanager.exceptions.DatabaseCreationException;

/**
 * The base class for the database manager, allows for creation and management
 * of databases.
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SpigotDatabaseManager {

    /**
     * The singleton instance of the database manager
     */
    private static final SpigotDatabaseManager s_instance;
    
    /** All databases which have been created. */
    private final List<IDatabase> m_databases = new ArrayList<IDatabase>();
    
    /** The Plugin instance backing this instance */
    private SpigotDatabaseManagerPlugin m_plugin = null;
    
    /**
     * Static constructor
     */
    static {
        s_instance = new SpigotDatabaseManager();
    }
    
    /**
     * Access to the singleton instance.
     *
     * @return The database manager instance
     */
    public static SpigotDatabaseManager getInstance() {
        return s_instance;
    }
    
    /**
     * Get the plugin that contains the database manager.
     *
     * @return The java plugin
     */
    public SpigotDatabaseManagerPlugin getPlugin() {
        return m_plugin;
    }
    
    /**
     * Set the plugin that contains the database manager.
     *
     * @param plugin The SpigotDatabaseManagerPlugin
     */
    public void setPlugin(SpigotDatabaseManagerPlugin plugin) {
        Preconditions.checkState(plugin == null, "Cannot reset plugin instance");
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
     *
     * @param <T> The type of database required
     * @param owner the plugin that is to own the new database
     * @param databaseType the database type to be created
     * @return the created database, ready to be setup
     * @throws DatabaseCreationException signals that the database couldn't be created
     */
    public static <T extends IDatabase> T createDatabase(JavaPlugin owner, Class<T> databaseType) throws DatabaseCreationException {
        try {
            // Create the new database
            T database = (T) databaseType.newInstance();


            // Set the owner and start the update thread
            database.setOwner(owner);
            database.start();

            // Store the database
            s_instance.m_databases.add(database);

            // Return the fully setup database
            return database;
        } catch (ReflectiveOperationException ex) {
            throw new DatabaseCreationException(ex);
        }
    }
    
    /**
     * Log an message to the console.
     *
     * @param message The message to log to the console
     */
    public static synchronized void log(String message) {
        getInstance().getPlugin().getLogger().log(Level.INFO, message);
    }
    
    /**
     * Log an exception to the console.
     *
     * @param message The user friendly context of the exception
     * @param ex The exception which occurred
     */
    public static synchronized void logException(String message, Exception ex) {
        getInstance().getPlugin().getLogger().log(Level.SEVERE, message, ex);
    }
    
}
