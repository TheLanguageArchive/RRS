/*
 * Constants.java
 *
 * Created on January 4, 2007, 3:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

/**
 *
 * @author kees
 */
public class Constants {
    
    /** Creates a new instance of Constants */
    public Constants() {
    }
    
    public final static String CORPUS_TEST_SERVER_HOSTNAME = "lux16";
    public final static String CORPUS_SERVER_HOSTNAME = "lux08";
    
    public final static String CORPUS_TEST_SERVER_JDBC_URL = "jdbc:postgresql://" + CORPUS_TEST_SERVER_HOSTNAME + "/corpusstructure";
    public final static String CORPUS_TEST_SERVER_DB_USER = "webuser";
    public final static String CORPUS_TEST_SERVER_DB_PASS = "start1a";
    public final static String CORPUS_TEST_SERVER_CONFIGDIR = "/srv/handlesystem/data"; 
    
    public final static String CORPUS_SERVER_JDBC_URL = "jdbc:postgresql://" + CORPUS_SERVER_HOSTNAME + ".mpi.nl:5432/corpusstructure";
    public final static String CORPUS_SERVER_DB_USER = "webuser";
    public final static String CORPUS_SERVER_DB_PASS = "v3Py(gc.k";
    public final static String CORPUS_SERVER_CONFIGDIR = "/home/kees/repaircheck/server";
    
    public final static String AMS_SERVER_JDBC_URL = "jdbc:postgresql://" + CORPUS_SERVER_HOSTNAME + ".mpi.nl:5432/ams";
    
}
