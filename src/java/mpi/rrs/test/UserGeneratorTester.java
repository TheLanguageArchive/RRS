/*
 * UserGeneratorTester.java
 *
 * Created on February 8, 2007, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */

import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
import java.sql.*;
import mpi.rrs.test.Constants;
import mpi.rrs.*;
import mpi.rrs.model.user.User;
import mpi.rrs.model.user.UserGenerator;
import org.postgresql.Driver;

public class UserGeneratorTester {
    
    static Connection amsDbConnection;
    
    /** Creates a new instance of UserGeneratorTester */
    public UserGeneratorTester() {
        
        String jdbcURLAms = Constants.AMS_SERVER_JDBC_URL;
        
        String user = Constants.CORPUS_SERVER_DB_USER;
        String pass = Constants.CORPUS_SERVER_DB_PASS;
        
        try {
            try {
                Class cl = Class.forName( "org.postgresql.Driver" ); //load the driver
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } 
            
            amsDbConnection = DriverManager.getConnection(jdbcURLAms, user, pass);
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) {
        UserGeneratorTester me = new UserGeneratorTester();
        
        me.showUserInfo("kees2");
        me.showUserInfo("kees");
        me.showUserInfo("rskiba");
        
        try {            
            amsDbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void showUserInfo(String userName) {
        UserGenerator ug = new UserGenerator(amsDbConnection);
        if (ug.isValidUserName(userName)) {
            User user = ug.getUserInfoByUserName(userName);
            System.out.println("Username: " + userName);
            System.out.println("Lastname: " + user.getLastName());
        } else {
            System.out.println("Invalid username: " + userName);
        }
        
    }
    
}
