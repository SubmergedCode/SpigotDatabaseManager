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
package uk.submergedcode.spigotdatabasemanager.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import uk.submergedcode.spigotdatabasemanager.SpigotDatabaseManager;
import uk.submergedcode.spigotdatabasemanager.annotations.TableName;
import uk.submergedcode.spigotdatabasemanager.annotations.TableValue;

/**
 * Abstract database class.
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public abstract class IDatabase extends Thread {
    
    /** Is the database thread running */
    private boolean m_running = false;
    
    /** The number of seconds to sleep every update */
    private int m_updateSleepLength = 5;
    
    /** The list of queries to execute */
    private final List<String> m_queries = new ArrayList<String>();
    
    /** The plugin of which this database belongs too. */
    private JavaPlugin m_owner;
    
    /**
     * Set the plugin which owns this database.
     *
     * @param owner The owner of the database
     */
    public synchronized void setOwner(JavaPlugin owner) {
        m_owner = owner;
    }
    
    /**
     * Get the type of database this class represents.
     *
     * @return The database type as a string
     */
    public abstract String getDatabseType();
 
    /**
     * Update the database, executing all database queries.
     *
     * @param queries A list of queries to execute
     * @return Called every time the database needs to update
     */
    public abstract boolean executeQueries(List<String> queries);
    
    /**
     * Free all allocated resources.
     */
    public abstract void shutdown();
    
    /**
     * Set the how many seconds the update thread sleeps for.
     *
     * @param updateTime The number of seconds
     */
    public synchronized void setUpdateTime(int updateTime) {
        m_updateSleepLength = updateTime;
    }
    
    /**
     * Set the running state of the database thread.
     *
     * @param running The state to set too
     */
    public synchronized void setRunning(boolean running) {
        m_running = running;
    }
    
    /**
     * Get the running state of the database thread.
     *
     * @return The running state
     */
    public synchronized boolean isRunning() {
        return m_running;
    }
    
    /**
     * Save some data to the database.
     *
     * @param data The data to save
     */
    public void saveTable(Object data) {
        
        Class dataClass = Object.class;
        if (!dataClass.isAnnotationPresent(TableName.class)) {
            // Not an annotated set of data
            return;
        }
        
        String tableName = ((TableName) dataClass.getAnnotation(TableName.class)).name();
        
        Field[] fields = dataClass.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableValue.class)) {
                TableValue tableValue = field.getAnnotation(TableValue.class);
                
            }
        }
    }
    
    /**
     * Copy the list of queries to a local list and execute them.
     */
    private void prepareAndExecuteQueries() {
        List<String> localQueries = new ArrayList<String>();
        synchronized(this) {
            localQueries.addAll(m_queries);
            m_queries.clear();
        }            
        executeQueries(localQueries);
    }
    
    /**
     * The thread run method.
     */
    @Override
    public void run() {
        
        setRunning(true);
        
        while (isRunning()) {
            
            prepareAndExecuteQueries();
                        
            try {
				Thread.sleep(m_updateSleepLength * 1000);
			} catch (InterruptedException ex) {
                SpigotDatabaseManager.logException("Exception whilst sleeping database.", ex);
			}
            
        }
        
        prepareAndExecuteQueries();
        shutdown();
    }
    
}
