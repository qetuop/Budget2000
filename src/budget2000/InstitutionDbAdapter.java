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
public class InstitutionDbAdapter extends AbstractDbAdapter {

    public InstitutionDbAdapter() {
        super();
        
        THIS_TABLE = TABLE_INSTITUTION;
    }

    public int createInstitution(Institution inst) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            String sql = String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s');",
                    THIS_TABLE, COLUMN_INSTITUTION_NAME, COLUMN_INSTITUTION_USER_ID,
                    inst.getInstitutionName(), inst.getUserId() );

            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            c.commit();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e.getClass().getName() + ": " + e.getMessage());
        }
        
        inst.setId(generatedKey);
        return generatedKey;

    } // createUser
    
    private Institution cursorToObject(ResultSet rs) {
        Institution x = null;

        try {
            if (rs != null) {
                
                int id = rs.getInt(COLUMN_ID);
                String institutionName = rs.getString(COLUMN_INSTITUTION_NAME);
                Integer userId = rs.getInt(COLUMN_INSTITUTION_USER_ID);
                
                x = new Institution(id, institutionName, userId);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":cursorToObject: " + e);
        }

        return x;

    } // cursorToUser
    
    // SELECT ALL
    public ArrayList<Institution> getAll() {
        ArrayList<Institution> objects = new ArrayList<>();

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s;", THIS_TABLE);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Institution x = cursorToObject(rs);
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
    public ArrayList<Institution> getAllForUser(Integer user_id) {
        ArrayList<Institution> objects = new ArrayList<>();

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_INSTITUTION_USER_ID, user_id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Institution x = cursorToObject(rs);
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
