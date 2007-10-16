/*
 * UserGenerator.java
 *
 * Created on February 8, 2007, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model.user;

/**
 * implementation of UserGenerator using AMS-1 direclty accessed by sql
 * @author kees
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mpi.lana.auth.UnixCrypt;


public class UserGenerator1 implements UserGenerator {
    
    private static Connection con;
    
    /** Creates a new instance of UserGenerator */
    public UserGenerator1(Connection con) {
        
        if (con != null) {
            try {
                con.setReadOnly(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            this.con = con;
        }
        
    }
    
    /**
	 * @see mpi.rrs.model.user.UserGeneratorIF#isValidPasswordForUsername(java.lang.String, java.lang.String)
	 */
    public boolean isValidPasswordForUsername(String userName, String passWord) {
        String sql = "select password from users where user_name = ?";
        
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, userName);
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                String DbpassWord = result.getString(1);
                if (DbpassWord == null) {
                    return false;
                } else if (UnixCrypt.matches(DbpassWord, passWord)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    /**
	 * @see mpi.rrs.model.user.UserGeneratorIF#isValidUserName(java.lang.String)
	 */
//    public boolean isValidUserName(String userName) {
//        String sql = "select count(*) from users where user_name = ?";
//        
//        PreparedStatement statement;
//        try {
//            statement = con.prepareStatement(sql);
//            statement.setString(1, userName);
//            statement.execute();
//            
//            ResultSet result = statement.getResultSet();
//            if (result.next()) {
//                if (result.getInt(1) == 0) {
//                    return false;
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        
//        return true;
//    }
    
    /**
	 * @see mpi.rrs.model.user.UserGeneratorIF#getUserInfoByUserName(java.lang.String)
	 */
    public User getUserInfoByUserName(String userName) {
        
        User user = new User();
        
        String sql = "select user_name," +
                "            first_name," +
                "            middle_name," +
                "            last_name," +
                "            creator," +
                "            creation_ts," +
                "            address," +
                "            affiliation," +
                "            password," +
                "            email" +
                "    from users" +
                "    where user_name = ?";
        
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, userName);
            statement.execute();
            
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                user.setUserName(result.getString("user_name"));
                user.setFirstName(result.getString("first_name"));
                user.setMiddleName(result.getString("middle_name"));
                user.setLastName(result.getString("last_name"));
                user.setCreator(result.getString("creator"));
                user.setCreation_ts(result.getString("creation_ts"));
                user.setAddress(result.getString("address"));
                user.setAffiliation(result.getString("affiliation"));
                user.setPassword(result.getString("password"));
                user.setEmail(result.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return user;
        
    }
    
    
    
}