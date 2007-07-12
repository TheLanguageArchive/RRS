/*
 * CorpusNodes.java
 *
 * Created on January 4, 2007, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

import mpi.corpusstructure.*;

import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
import java.sql.*;
import mpi.rrs.test.Constants;
import mpi.rrs.test.RrsUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;

/**
 * class CorpusNodes <br>
 * general methods to get and set URID info
 *
 * Note: there are 2 databases involved: <br>
 * corpusstructure  (table archiveobjects)
 * handlesystem (table handles)
 *
 */
public class CorpusNodes {
    // Define a static logger variable so that it references the
    // Logger instance named "CorpusNodes".
    static Logger logger = Logger.getLogger(CorpusNodes.class);
    
    private static String jdbcURL;
    private static String user;
    private static String pass;
    private static String configdir;
    
    public static CorpusStructureDBImpl csdb;
    
    public static void setDBCredentials() {
        if (RrsUtil.runsOnCorpusTestserver()) {
            jdbcURL = Constants.CORPUS_TEST_SERVER_JDBC_URL;
            user = Constants.CORPUS_TEST_SERVER_DB_USER;
            pass = Constants.CORPUS_TEST_SERVER_DB_PASS;
            
        } else {
            jdbcURL = Constants.CORPUS_SERVER_JDBC_URL;
            user = Constants.CORPUS_SERVER_DB_USER;
            pass = Constants.CORPUS_SERVER_DB_PASS;
            
        }
        
        logger.debug("setDBCredentials");
        logger.debug("TestServer: " + RrsUtil.runsOnCorpusTestserver());
        logger.debug("jdbcURL: " + jdbcURL);
        
        csdb = new CorpusStructureDBImpl( jdbcURL,false,user,pass);
        
    }
    
    /** Creates a new instance of CorpusNodes */
    public CorpusNodes() {
        // Configure logger
        logger.setLevel(Level.DEBUG);
        logger.debug("CorpusNodes:" + logger.getName());
        
        if (csdb == null) {
            setDBCredentials();
        }
        
    }
    
    public CorpusNodes(CorpusStructureDBImpl csdb) {
        this.csdb = csdb;
    }
    
    public String getNodeName(String nodeId) {
        String nodeName = "UNKNOWN";
        if (csdb == null) {
            this.setDBCredentials();
        }
        
        if (csdb == null) {
            logger.debug("csdb is null");
        } else {
            try {
                
                if (csdb.getNode(nodeId) != null) {
                
                nodeName = csdb.getNode(nodeId).getName();
                }
                else {
                    logger.debug("Node is null for nodeId: " + nodeId);
                }
            } catch (UnknownNodeException ex) {
                ex.printStackTrace();
            }
        }
        
        return nodeName;
        
    }
    
}
