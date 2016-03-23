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
public class AccountDbAdapter extends AbstractDbAdapter {

    public AccountDbAdapter() {
        super();
        
        THIS_TABLE = TABLE_ACCOUNT;
    }

    public int createAccount(Account account) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            
            String sql = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s');",
                    THIS_TABLE, COLUMN_ACCOUNT_NAME, COLUMN_ACCOUNT_INSTITUTION_ID,
                    account.getAccountName(), account.getInstitutionId() );

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            conn.commit();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }
        
        account.setId(generatedKey);
        return generatedKey;

    } // createUser
    
    private Account cursorToObject(ResultSet rs) {
        Account x = null;

        try {
            if (rs != null) {
                
                int id = rs.getInt(COLUMN_ID);
                String accountName = rs.getString(COLUMN_ACCOUNT_NAME);
                Integer instId = rs.getInt(COLUMN_ACCOUNT_INSTITUTION_ID);
                
                x = new Account(id, accountName, instId);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":cursorToObject: " + e);
        }

        return x;

    } // cursorToUser
    
    // SELECT ALL
    public ArrayList<Account> getAll() {
        ArrayList<Account> objects = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s;", THIS_TABLE);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Account x = cursorToObject(rs);
                if (x != null) {
                    objects.add(x);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":getAll: " + e);
        }

        return objects;

    } // getUsers
    
    // SELECT ALL
    public ArrayList<Account> getAllForInstitution(Integer _id) {
        ArrayList<Account> objects = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_ACCOUNT_INSTITUTION_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Account x = cursorToObject(rs);
                if (x != null) {
                    objects.add(x);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":getAll: " + e);
        }

        return objects;

    } // getUsers
    
}
