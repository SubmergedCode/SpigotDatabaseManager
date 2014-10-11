/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.submergedcode.spigotdatabasemanager.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import uk.submergedcode.spigotdatabasemanager.SpigotDatabaseManager;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SQLiteDatabase extends IDatabase {

    /**
     * The type of this database
     */ 
    public static final String TYPE = "sqlite";
    
    /**
	 * The file that represents the SQLite database file.
	 */
	private File m_databaseFile = null;
    
    /**
     * Get the type of database this class represents.
     * @return The database type as a string
     */
    @Override
    public String getDatabseType() {
        return SQLiteDatabase.TYPE;
    }
    
    /**
     * Setup the SQLite database
     * @param databaseFile The file used as the SQLite database.
     * @param updateTime The number of seconds the update thread sleeps for between updates.
     */
    public void setup(File databaseFile, int updateTime) {
        m_databaseFile = databaseFile;
        setUpdateTime(updateTime);
    }
    
    /**
     * Get a connection to the SQLite database.
	 * @return  The SQLite connection, or null on failure
	 */
	private Connection getConnection() {
        
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			SpigotDatabaseManager.logException("Could not find sqlite drivers.", ex);
			return null;
		}
		
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + m_databaseFile.getAbsolutePath());
		} catch (SQLException ex) {
            SpigotDatabaseManager.logException("Could not create sqlite connection to '" + m_databaseFile.getAbsolutePath() + "'.", ex);
			return null;
		}
        
	}
    
    /**
     * Update the database
     * Called every time the database needs to update
     * @param queries A list of queries to execute.
     * @return true to continue running, false will stop the database thread
     */
    @Override
    public boolean executeQueries(List<String> queries) {
        
        // Nothing to do
        if (queries.isEmpty()) {
            return true;
        }
        
        // Try get the connection to the database
        final Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
        String currentQuery = null;
        Statement currentStatement = null;
        
        try {
            currentStatement = connection.createStatement();
            
            Iterator<String> queryIterator = queries.iterator();
            while (queryIterator.hasNext()) {
                try {
                    currentQuery = queryIterator.next();
                    currentStatement.addBatch(currentQuery);
                }
                catch(SQLException ex) {
                    SpigotDatabaseManager.logException("Problem executing SQLite query '" + currentQuery + "'.", ex);   
                }
            }		
            currentStatement.executeBatch();
        }
        catch(SQLException ex) {
            SpigotDatabaseManager.logException("Problem executing SQLite batch.", ex);           
        }
        finally {
            try {
                if (currentStatement != null) {
                    currentStatement.close();
                }
                connection.close();
            }
            catch(SQLException ex) {
                SpigotDatabaseManager.logException("Problem closing SQLite connection", ex);
            }
        }
        
        return true;
    }

    /**
     * Free all allocated resources
     */
    @Override
    public void shutdown() {
        
        this.setRunning(false);
        
    }
    
}
