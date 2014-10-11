package uk.submergedcode.spigotdatabasemanager;

import org.junit.Test;
import uk.submergedcode.spigotdatabasemanager.database.SQLiteDatabase;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SpigotDatabaseManagerTest {
    
    /**
     * Test creating an SQLite database
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testCreateSQLiteDatabase() throws InstantiationException, IllegalAccessException {
        SQLiteDatabase database = SpigotDatabaseManager.createDatabase(null, SQLiteDatabase.class);
        database.shutdown();
    }

}
