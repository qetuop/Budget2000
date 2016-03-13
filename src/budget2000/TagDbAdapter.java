/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import static budget2000.AbstractDbAdapter.c;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author brian
 */
public class TagDbAdapter extends AbstractDbAdapter {
    
    public TagDbAdapter() {
        super();
        
        THIS_TABLE = TABLE_TAG;
    }
    
    public int createTag(Tag tag) {
        Statement stmt = null;
        int generatedKey = 0;

        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            String sql = String.format("INSERT INTO %s (%s) VALUES ('%s');",
                    THIS_TABLE, COLUMN_TAG_NAME,
                    tag.getName() );

            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
                           
            if (rs.next()) {
                generatedKey = rs.getInt(1); // long or int?
            }

            stmt.close();
            c.commit();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }
        
        tag.setId(generatedKey);
        return generatedKey;

    } // createUser
    
    private Tag cursorToObject(ResultSet rs) {
        Tag x = null;

        try {
            if (rs != null) {
                
                int id = rs.getInt(COLUMN_ID);
                String name = rs.getString(COLUMN_TAG_NAME);
                
                x = new Tag(id, name);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return x;

    } // cursorToUser
    
    // READ one - id
    public Tag getTag(Integer _id) {
        Tag tag = null;

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE %s = %d;",
                    THIS_TABLE, COLUMN_ID, _id);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            if (rs.next()) {
                tag = cursorToObject(rs);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getName() + ": " + e.getMessage());
        }

        return tag;

    } // getUser
    
    // SELECT ALL
    public ArrayList<Tag> getAll() {
        ArrayList<Tag> objects = new ArrayList<>();

        try {
            Statement stmt = c.createStatement();
            String sql = String.format("SELECT * FROM %s;", THIS_TABLE);

            ResultSet rs = stmt.executeQuery(sql); // executeQuery

            // should only be one. i hope. whats a better way
            while (rs.next()) {

                Tag x = cursorToObject(rs);
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
}
