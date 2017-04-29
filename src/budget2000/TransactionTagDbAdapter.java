/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import static budget2000.AbstractDbAdapter.conn;

/**
 *
 * @author brian
 */
public class TransactionTagDbAdapter extends AbstractDbAdapter {
    
    public TransactionTagDbAdapter() {
        super();
        
        THIS_TABLE = TABLE_TRANSACTION_TAG;
    }
    
    public int createTransactionTag(TransactionTag transactionTag) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            
            String sql = String.format("INSERT INTO %s (%s, %s) VALUES (%d, %d);",
                    THIS_TABLE, COLUMN_TRANSACTION_TAG_TRANSACTION_ID, COLUMN_TRANSACTION_TAG_TAG_ID,
                    transactionTag.getTransactionId(), transactionTag.getTagId());
logger.info("SQL:" +sql);
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            conn.commit();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }
        transactionTag.setId(generatedKey);
        return generatedKey;

    } // createUser
    
    private TransactionTag cursorToObject(ResultSet rs) {
        TransactionTag x = null;

        try {
            if (rs != null) {
                
                int id = rs.getInt(COLUMN_ID);
                int transactionId = rs.getInt(COLUMN_TRANSACTION_TAG_TRANSACTION_ID);
                int tagId = rs.getInt(COLUMN_TRANSACTION_TAG_TAG_ID);
                
                x = new TransactionTag(id, transactionId, tagId);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return x;

    } // cursorToUser
    
    // READ one - id
    public TransactionTag getTransactionTag(Integer _id) {
        TransactionTag x = null;

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    TABLE_USER, COLUMN_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            if (rs.next()) {
                x = cursorToObject(rs);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return x;

    } // getUser
    
    // SELECT ALL
    public ArrayList<TransactionTag> getAll() {
        ArrayList<TransactionTag> objects = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s;", THIS_TABLE);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                TransactionTag x = cursorToObject(rs);
                if (x != null) {
                    objects.add(x);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return objects;

    } // getUsers
    
    // SELECT ALL  for a transaction id
    public ArrayList<TransactionTag> getAllForTransaction(Integer _id) {
        ArrayList<TransactionTag> objects = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_TRANSACTION_TAG_TRANSACTION_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                TransactionTag x = cursorToObject(rs);
                if (x != null) {
                    objects.add(x);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return objects;

    } // 
    
    // SELECT ALL  for a transaction id
    public ArrayList<TransactionTag> getAllForTag(Integer _id) {
        ArrayList<TransactionTag> objects = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_TRANSACTION_TAG_TAG_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                TransactionTag x = cursorToObject(rs);
                if (x != null) {
                    objects.add(x);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return objects;

    } // 
    
    
    // TODO: can i create the DB schema to prevent duplicates from being added
    // in the first place?
    
    // 
    public Boolean exists(Transaction transaction, Tag tag) {
        Boolean exists = true;

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d AND %s  = %d;",                    
                    THIS_TABLE, 
                    COLUMN_TRANSACTION_TAG_TRANSACTION_ID, transaction.getId(),
                    COLUMN_TRANSACTION_TAG_TAG_ID, tag.getId()
            );
            //logger.info(sql);
            //System.out.println("sql= " + sql);       
            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // isBeforeFirst  returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
            if ( !rs.isBeforeFirst() ) {
                //System.out.println("rs is empty");
                exists = false;
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return exists;

    } // exists
    
    // DELETE all entries with this transaction ID
    public void deleteTransaction(Integer _id) {
       try {            
            String sql = String.format("DELETE FROM %s WHERE %s=%d;",
                    THIS_TABLE, COLUMN_TRANSACTION_TAG_TRANSACTION_ID, _id);
            
            logger.info("SQL:" +sql);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql); // executeUpdate
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            logger.warning(e.toString());
        }

    } // 
}
