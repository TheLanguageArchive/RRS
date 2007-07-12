/*
 * NodeGeneratorTester.java
 *
 * Created on February 8, 2007, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
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
import mpi.rrs.model.corpusdb.ImdiNodeGenerator;
import org.postgresql.Driver;

public class ImdiNodeGeneratorTester {
    
    static Connection corpusDbConnection;
    
    /** Creates a new instance of ImdiNodeGeneratorTester */
    public ImdiNodeGeneratorTester() {
        
        String jdbcURLCorpus = Constants.CORPUS_SERVER_JDBC_URL;
        
        String user = Constants.CORPUS_SERVER_DB_USER;
        String pass = Constants.CORPUS_SERVER_DB_PASS;
        
        try {
            try {
                Class cl = Class.forName( "org.postgresql.Driver" ); //load the driver
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } 
            
            corpusDbConnection = DriverManager.getConnection(jdbcURLCorpus, user, pass);
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) {
        ImdiNodeGeneratorTester me = new ImdiNodeGeneratorTester();
        
        me.showImdiNodeInfo("363998");
        me.showImdiNodeInfo("rskiba");
        
        try {            
            corpusDbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void showImdiNodeInfo(String nodeId) {
        ImdiNodeGenerator ng = new ImdiNodeGenerator(corpusDbConnection);
        if (ng.isExistingNodeIdInDb(nodeId)) {
            
            System.out.println("nodeId: " + nodeId);
        } else {
            System.out.println("Invalid nodeId: " + nodeId);
        }
        
    }
    
}
