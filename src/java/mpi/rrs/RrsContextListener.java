/*
 * RrsContextListener.java
 *
 * Created on February 5, 2007, 2:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs;

/**
 *
 * @author kees
 */

import javax.servlet.*;
import mpi.corpusstructure.*;
import mpi.rrs.model.utilities.RrsUtil;

import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
import java.sql.*;
import org.apache.log4j.*;

import mpi.rrs.model.RrsRequest;

public class RrsContextListener implements ServletContextListener {
    
    static Logger logger = Logger.getLogger("RrsContextListener");
    
    private String corpusJdbcURL;
    private String corpusUser;
    private String corpusPass;
    
    private String amsJdbcURL;
    private String amsUser;
    private String amsPass;
    
    private CorpusStructureDBImpl corpusDbConnection;
    private Connection amsDbConnection;
    
    
    /** Creates a new instance of RrsContextListener */
    public RrsContextListener() {
    }
    
    public void contextInitialized(ServletContextEvent sce) {     
        
        ServletContext sc = sce.getServletContext();
        
        String maxFormNodeIds = sc.getInitParameter("MAX_FORM_NODE_IDS");
        sc.setAttribute("maxFormNodeIds", maxFormNodeIds);
        
        String emailAddressCorpman = sc.getInitParameter("EMAIL_ADDRESS_CORPMAN");
        sc.setAttribute("emailAddressCorpman", emailAddressCorpman);
        
        String prefix =  sc.getRealPath("/");
        String file = sc.getInitParameter("LOG4J_PROPERTIES_FILE");
        // if the log4j-init-file is not set, then no point in trying
        if( file != null ) {
            PropertyConfigurator.configure(prefix+file);
        }
        
        
        logger.debug("RrsContextListener contextInitialized");
        
        try {
            Class cl = Class.forName( "org.postgresql.Driver" );
        } catch (ClassNotFoundException ex) {
            logger.error("contextInitialized: No org.postgresql.Driver", ex);
            //ex.printStackTrace();
        }
        
        String testServer = sc.getInitParameter("TEST_CORPUS_SERVER_HOSTNAME");
        String prodServer = sc.getInitParameter("CORPUS_SERVER_HOSTNAME");
        
        
        corpusJdbcURL = sc.getInitParameter("defaultIMDIDB");
        logger.debug("corpusJdbcURL: " + corpusJdbcURL);
        
        amsJdbcURL = sc.getInitParameter("RRS_AMS_SERVER_JDBC_URL");
        amsUser = sc.getInitParameter("RRS_AMS_SERVER_DB_USER");
        amsPass = sc.getInitParameter("RRS_AMS_SERVER_DB_PASS");
        
        corpusJdbcURL = sc.getInitParameter("RRS_CORPUS_SERVER_JDBC_URL");
        corpusUser = sc.getInitParameter("RRS_CORPUS_SERVER_DB_USER");
        corpusPass = sc.getInitParameter("RRS_CORPUS_SERVER_DB_PASS");
        
        if (corpusJdbcURL == null) {
            // get local context params
            logger.debug("RRS_CORPUS_SERVER_JDBC_URL == null");
            corpusJdbcURL = sc.getInitParameter("TEST_CORPUS_SERVER_JDBC_URL");
            corpusUser = sc.getInitParameter("TEST_CORPUS_SERVER_DB_USER");
            corpusPass = sc.getInitParameter("TEST_CORPUS_SERVER_DB_PASS");
        } else {
            logger.debug("RRS_CORPUS_SERVER_JDBC_URL != null");
        }
        
        if (amsJdbcURL == null) {
            // get local context params
            amsJdbcURL = sc.getInitParameter("RRS_V1_AMS_SERVER_JDBC_URL");
            amsUser = sc.getInitParameter("RRS_V1_AMS_SERVER_DB_USER");
            amsPass = sc.getInitParameter("RRS_V1_AMS_SERVER_DB_PASS");
        }
        
        try {
            boolean bootstrapMode = false;
            corpusDbConnection = new CorpusStructureDBImpl(corpusJdbcURL, bootstrapMode, corpusUser, corpusPass);
            
            amsDbConnection = DriverManager.getConnection(amsJdbcURL, amsUser, amsPass);
            
        } catch (SQLException ex) {
            corpusDbConnection = null;
            amsDbConnection = null;
            logger.error("contextInitialized: No db connection", ex);
            
        } finally {
            sc.setAttribute("amsDbConnection", amsDbConnection);
            sc.setAttribute("corpusDbConnection", corpusDbConnection);
        }
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");
        if (corpusDbConnection != null) {
            logger.info("Closing Corpus DB");
            corpusDbConnection.close();      
        }
        
        try {
            if (amsDbConnection != null) {
                logger.info("Closing Ams DB");
                amsDbConnection.close();
            }
        } catch (SQLException ex) {
            logger.error("contextDestroyed: unable to close AMS db", ex);
            //ex.printStackTrace();
        }
    }
}
