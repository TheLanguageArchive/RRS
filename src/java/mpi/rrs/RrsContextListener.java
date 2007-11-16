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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mpi.corpusstructure.CorpusStructureDBImpl;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RrsContextListener implements ServletContextListener {
    
    static Logger logger = Logger.getLogger("RrsContextListener");
    
    private String corpusJdbcURL;
    private String corpusUser;
    private String corpusPass;
    
    //private String amsJdbcURL;
    //private String amsUser;
    //private String amsPass;
    
    private CorpusStructureDBImpl corpusDbConnection;
    //private Connection amsDbConnection;
    
    
    /** Creates a new instance of RrsContextListener */
    public RrsContextListener() {
    }
    
    public void contextInitialized(ServletContextEvent sce) {     
        
        ServletContext sc = sce.getServletContext();
        
        String maxFormNodeIds = sc.getInitParameter("MAX_FORM_NODE_IDS");
        sc.setAttribute("maxFormNodeIds", maxFormNodeIds);
        
        String emailAddressCorpman = sc.getInitParameter("EMAIL_ADDRESS_CORPMAN");
        sc.setAttribute("emailAddressCorpman", emailAddressCorpman);
        
        String emailHost = sc.getInitParameter("EMAIL_HOST");
        sc.setAttribute("emailHost", emailHost);
        
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
            logger.error("RRS_V1 ***** contextInitialized: No org.postgresql.Driver", ex);
            //ex.printStackTrace();
        }
        
        
        // TODO: still necessary?!
        // <=> replace by ams2 config parameter!?
        //amsJdbcURL = sc.getInitParameter("RRS_AMS_SERVER_JDBC_URL");
        //amsUser = sc.getInitParameter("RRS_AMS_SERVER_DB_USER");
        //amsPass = sc.getInitParameter("RRS_AMS_SERVER_DB_PASS");
        
        corpusJdbcURL = sc.getInitParameter("RRS_V1_CORPUS_SERVER_JDBC_URL");
        corpusUser = sc.getInitParameter("RRS_V1_CORPUS_SERVER_DB_USER");
        corpusPass = sc.getInitParameter("RRS_V1_CORPUS_SERVER_DB_PASS");
        
        
        //try {
            boolean bootstrapMode = false;
            corpusDbConnection = new CorpusStructureDBImpl(corpusJdbcURL, bootstrapMode, corpusUser, corpusPass);
            if (corpusDbConnection == null) {
                logger.fatal("************ corpusDbConnection is null");
                logger.fatal("corpusJdbcURL: " + corpusJdbcURL);
                logger.fatal("corpusUser: " + corpusUser);
            }
            
            //amsDbConnection = DriverManager.getConnection(amsJdbcURL, amsUser, amsPass);
            
        //} catch (SQLException ex) {
            //corpusDbConnection = null;
            //amsDbConnection = null;
            //logger.error("contextInitialized: No db connection", ex);
            
        //} finally {
            //sc.setAttribute("amsDbConnection", amsDbConnection);
            sc.setAttribute("corpusDbConnection", corpusDbConnection);
        //}
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");
        if (corpusDbConnection != null) {
            logger.info("Closing Corpus DB");
            corpusDbConnection.close();      
        }
        
        /*
        try {
            if (amsDbConnection != null) {
                logger.info("Closing Ams DB");
                amsDbConnection.close();
            }
        } catch (SQLException ex) {
            logger.error("contextDestroyed: unable to close AMS db", ex);
            //ex.printStackTrace();
        }
         */
    }
}
