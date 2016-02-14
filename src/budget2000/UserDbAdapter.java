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
public class UserDbAdapter extends AbstractDbAdapter {
    
    public UserDbAdapter() {
        super();
        
        THIS_TABLE = TABLE_USER;
    }

    // CREATE
    public int createUser(User user) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s');",
                    TABLE_USER, COLUMN_USER_FIRST_NAME, COLUMN_USER_LAST_NAME,
                    user.getFirstName(), user.getLastName());

            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return generatedKey;

    } // createUser

    private User cursorToUser(ResultSet rs) {
        User user = null;

        try {
            if (rs != null) {
                int id = rs.getInt(COLUMN_ID);
                String firstName = rs.getString(COLUMN_USER_FIRST_NAME);
                String lastName = rs.getString(COLUMN_USER_LAST_NAME);

                user = new User(id, firstName, lastName);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return user;

    } // cursorToUser

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

    // SELECT ALL
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s;", TABLE_USER);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                User user = cursorToUser(rs);
                if (user != null) {
                    users.add(user);
                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }

        return users;

    } // getUsers

    // UPDATE
    public void update(User user) {
        try {
            String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = %d;",
                    TABLE_USER, COLUMN_USER_FIRST_NAME, COLUMN_USER_LAST_NAME, COLUMN_ID, user.getId());
            PreparedStatement update = c.prepareStatement(sql);

            update.setString(1, user.getFirstName());
            update.setString(2, user.getLastName());

            update.executeUpdate();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
//    // DELETE
//    public void delete(Integer _id) {
//        try {
//            Statement stmt = c.createStatement();
//            String sql = String.format("DELETE FROM %s WHERE %s=%d;",
//                    TABLE_USER, COLUMN_ID, _id);
//
//            stmt.executeUpdate(sql); // executeUpdate
//            stmt.close();
//            c.commit();
//        } catch (Exception e) {
//            System.err.println(this.getClass().getName() + ":delete: " + e);
//        }
//    } // delete   
}
