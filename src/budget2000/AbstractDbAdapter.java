/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.sql.*;
import java.util.logging.Logger;

/**
 *
 * @author brian
 */
public class AbstractDbAdapter {

    protected static Logger logger = Logger.getGlobal();
    
    protected static Connection conn = null;
    private static Boolean databaseCreated = false;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "budget2000.db";

    // Table Names
    public static final String TABLE_USER = "user";
    public static final String TABLE_INSTITUTION = "institution";
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_TRANSACTION = "transaction1";
    public static final String TABLE_TAG = "tag";
    public static final String TABLE_TRANSACTION_TAG = "transaction_tag";
    
    protected String THIS_TABLE = null;

    // Common
    public static final String COLUMN_ID = "_id"; //

    // User TABLE
    public static final String COLUMN_USER_FIRST_NAME = "first_name";
    public static final String COLUMN_USER_LAST_NAME = "last_name";

    // Institution TABLE
    public static final String COLUMN_INSTITUTION_NAME = "name";
    public static final String COLUMN_INSTITUTION_USER_ID = "user_id";

    // Account TABLE
    public static final String COLUMN_ACCOUNT_NAME = "name";
    public static final String COLUMN_ACCOUNT_INSTITUTION_ID = "institution_id";

    // Transaction TABLE
    public static final String COLUMN_TRANSACTION_DATE = "date";
    public static final String COLUMN_TRANSACTION_NAME = "name";   
    public static final String COLUMN_TRANSACTION_DISPLAY_NAME = "display_name";   
    public static final String COLUMN_TRANSACTION_AMOUNT = "amount"; 
    public static final String COLUMN_TRANSACTION_ACCOUNT_ID = "account_id";

    // Tag TABLE
    public static final String COLUMN_TAG_NAME = "name";
    
    // Transaction_Tag TABLE
    public static final String COLUMN_TRANSACTION_TAG_TRANSACTION_ID = "transaction_id";
    public static final String COLUMN_TRANSACTION_TAG_TAG_ID = "tag_id";

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_USER_FIRST_NAME + " text, "
            + COLUMN_USER_LAST_NAME + " text "
            + ");";  // no trailing ';'

    private static final String CREATE_TABLE_INSTITUTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSTITUTION + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_INSTITUTION_USER_ID + " integer not null references " + TABLE_USER + "(" + COLUMN_ID +") ON DELETE CASCADE, "
            + COLUMN_INSTITUTION_NAME + " text "           
            + ");";  // no trailing ';'
    
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACCOUNT + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_ACCOUNT_INSTITUTION_ID + " integer not null references " + TABLE_INSTITUTION + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_ACCOUNT_NAME + " text "            
            + ");";  // no trailing ';'
    
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRANSACTION + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_TRANSACTION_ACCOUNT_ID + " integer not null references " + TABLE_ACCOUNT + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_TRANSACTION_DATE + " integer not null, "
            + COLUMN_TRANSACTION_NAME + " text, "
            + COLUMN_TRANSACTION_DISPLAY_NAME + " text, "
            + COLUMN_TRANSACTION_AMOUNT + " decimal(10,2) "             
            + ");";  // no trailing ';'
    
    private static final String CREATE_TABLE_TAG = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TAG + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_TAG_NAME + " text unique "            
            + ");";  // no trailing ';'
    
    private static final String CREATE_TABLE_TRANSACTION_TAG = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRANSACTION_TAG + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_TRANSACTION_TAG_TRANSACTION_ID + " integer references " + TABLE_TRANSACTION + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_TRANSACTION_TAG_TAG_ID + " integer references " + TABLE_TAG + "(" + COLUMN_ID + ") ON DELETE CASCADE "            
            + ")";

//    private static final String CREATE_TABLE_TRANSACTION_TAG = "CREATE TABLE IF NOT EXISTS "
//            + TABLE_TRANSACTION_TAG + "("
//            + COLUMN_ID + " integer not null primary key autoincrement, "
//            
//            + COLUMN_TRANSACTION_TAG_TRANSACTION_ID + " integer, " 
//            + "FOREIGN KEY(" + COLUMN_TRANSACTION_TAG_TRANSACTION_ID +") REFERENCES " + TABLE_TRANSACTION + "(" + COLUMN_ID + ") ON DELETE CASCADE, "
//            
//            + COLUMN_TRANSACTION_TAG_TAG_ID + " integer, " 
//            + "FOREIGN KEY(" + COLUMN_TRANSACTION_TAG_TAG_ID +") REFERENCES " + TABLE_TAG + "(" + COLUMN_ID + ") ON DELETE CASCADE "          
//            
//            + ");";
    
    // TODO: is this a bad idea?
    public AbstractDbAdapter() {
        logger.info("");
        
        // each will return if already created
        createConnection();
        createDatabase();
    }
    
    public void createConnection() {                
        // Create connection
        if (conn != null) {
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
            logger.info("Connection created: " + conn);
            
             // necessary for stuff to work right - cascade
             Statement stmt = conn.createStatement(); 
             stmt.executeUpdate("PRAGMA foreign_keys = ON; ");
             stmt.close();
             
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    } // createConnection
    
    public void createDatabase() {                        
        if (databaseCreated == true) {
            return;
        }
        
        try {            
            for (String sql : new String[]{
                CREATE_TABLE_USER, 
                CREATE_TABLE_INSTITUTION, 
                CREATE_TABLE_ACCOUNT,
                CREATE_TABLE_TRANSACTION,
                CREATE_TABLE_TAG,
                CREATE_TABLE_TRANSACTION_TAG                 
            }) {
                logger.info("SQL:" +sql);                
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
            logger.info("Tables created");            
            databaseCreated = true;
            
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                conn = null; // TODO: is this right?
                logger.info("Connection Closed");
            }
        } catch (Exception e) {
            logger.warning(e.toString());
            System.exit(0);
        }
    }

    public void dropTables() {

        // tables with relationships must be dropped in correct order?
        // stmt = conn.createStatement(); 
        // stmt.executeUpdate("PRAGMA foreign_keys = OFF; ");
        
        try {
            
            for (String s : new String[]{
                TABLE_USER, 
                TABLE_INSTITUTION, 
                TABLE_ACCOUNT,
                TABLE_TRANSACTION_TAG,
                TABLE_TRANSACTION,
                TABLE_TAG                 
            }) {
                String sql = "DROP TABLE " + s;
                
                logger.info("SQL:" +sql);    
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
                logger.info(s + " dropped");
                databaseCreated = false;
            }
            
            // stmt = conn.createStatement(); 
            // stmt.executeUpdate("PRAGMA foreign_keys = ON; ");

        } catch (Exception e) {
            logger.warning(e.toString());
        }
    }
    
    // DELETE
    public void delete(Integer _id) {
        try {            
            String sql = String.format("DELETE FROM %s WHERE %s=%d;",
                    THIS_TABLE, COLUMN_ID, _id);
            
            logger.info("SQL:" +sql);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql); // executeUpdate
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            logger.warning(e.toString());
        }
    } // delete   

} // AbstractDbAdapter
