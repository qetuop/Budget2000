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

/**
 *
 * @author brian
 */
public class TransactionDbAdapter extends AbstractDbAdapter {

    public TransactionDbAdapter() {
        super();

        THIS_TABLE = TABLE_TRANSACTION;
    }

    // CREATE
    public int createTransaction(Transaction transaction) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s','%s', %f, %d);",
                    THIS_TABLE,
                    COLUMN_TRANSACTION_DATE,
                    COLUMN_TRANSACTION_NAME,
                    COLUMN_TRANSACTION_DISPLAY_NAME,
                    COLUMN_TRANSACTION_AMOUNT,
                    COLUMN_TRANSACTION_ACCOUNT_ID,
                    transaction.getDate(),
                    transaction.getName(),
                    transaction.getDisplayName(),
                    transaction.getAmount(),
                    transaction.getAccountId());
            logger.info("SQL: " + sql);
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            conn.commit();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":createTransaction: " + e);
        }

        transaction.setId(generatedKey);
        return generatedKey;

    } // create

     // UPDATE
    public void updateTransaction(Transaction transaction) {
        Statement stmt = null;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            String sql = String.format("UPDATE %s SET %s = %d, %s = '%s', %s = '%s', %s = %f, %s = %d WHERE %s = %d",
                    THIS_TABLE,
                    COLUMN_TRANSACTION_DATE,
                    transaction.getDate(),
                    COLUMN_TRANSACTION_NAME,
                     transaction.getName(),
                    COLUMN_TRANSACTION_DISPLAY_NAME,
                    transaction.getDisplayName(),
                    COLUMN_TRANSACTION_AMOUNT,
                    transaction.getAmount(),
                    COLUMN_TRANSACTION_ACCOUNT_ID,
                    transaction.getAccountId(),
                    COLUMN_ID,
                    transaction.getId()
            );
logger.info(sql);
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            stmt.close();
            conn.commit();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":createTransaction: " + e);
        }
    } // update
    
    private Transaction cursorToTransaction(ResultSet rs) {
        Transaction transaction = null;

        try {
            if (rs != null) {
                int id = rs.getInt(COLUMN_ID);
                Long date = rs.getLong(COLUMN_TRANSACTION_DATE);
                String name = rs.getString(COLUMN_TRANSACTION_NAME);
                String displayName = rs.getString(COLUMN_TRANSACTION_DISPLAY_NAME);
                Double amount = rs.getDouble(COLUMN_TRANSACTION_AMOUNT);
                Integer accountId = rs.getInt(COLUMN_TRANSACTION_ACCOUNT_ID);
                transaction = new Transaction(id, date, name, displayName, accountId, amount);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return transaction;

    } // cursorToUser
/*
    // READ one - id
    public User getUser(Integer _id) {
        User user = null;

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    TABLE_USER, COLUMN_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            if (rs.next()) {
                user = cursorToUser(rs);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return user;

    } // getUser
     */
    // SELECT ALL
    public ArrayList<Transaction> getAll() {
        ArrayList<Transaction> users = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s;", THIS_TABLE);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Transaction t = cursorToTransaction(rs);
                if (t != null) {
                    users.add(t);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return users;

    } // getUsers

    // SELECT ALL
    public ArrayList<Transaction> getAllForAccount(Integer _id) {
        ArrayList<Transaction> users = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_TRANSACTION_ACCOUNT_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Transaction t = cursorToTransaction(rs);
                if (t != null) {
                    users.add(t);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return users;

    } // getAllForAccount
    
    // SELECT ALL
    public ArrayList<Transaction> getAllForName(String name) {
        ArrayList<Transaction> result = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = '%s';",
                    THIS_TABLE, COLUMN_TRANSACTION_NAME, name);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Transaction t = cursorToTransaction(rs);
                if (t != null) {
                    result.add(t);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.severe(e.toString());
        }

        return result;

    } // getAllForName
    
    
    /*
    // DELETE
    public void delete(Integer _id) {

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("DELETE FROM %s WHERE %s=%d;",
                    TABLE_USER, COLUMN_ID, _id);

            stmt.executeUpdate(sql); // executeUpdate
            stmt.close();
            c.commit();
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
     */
    
    // SELECT ALL
    public Boolean exists(Transaction transaction) {
        Boolean exists = true;

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d AND %s = '%s' AND %s = %d AND %s = %f;",                    
                    THIS_TABLE, 
                    COLUMN_TRANSACTION_ACCOUNT_ID, transaction.getAccountId(),
                    COLUMN_TRANSACTION_NAME, transaction.getName(),
                    COLUMN_TRANSACTION_DATE, transaction.getDate(),
                    COLUMN_TRANSACTION_AMOUNT, transaction.getAmount()
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

    } // getAllForAccount
    
} // TransactionDbAdapter
