/*
 * Util.java
 *
 * Created on January 4, 2007, 3:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class RrsUtil {
    static Logger logger = Logger.getLogger(RrsUtil.class);
    
    /** Creates a new instance of Util */
    private RrsUtil() {
    }
    
    /**
     * test if program runs on corpus test server
     * @return true if program runs on corpus test server
     */
    public static boolean runsOnCorpusTestserver() {
        if (Constants.CORPUS_TEST_SERVER_HOSTNAME.equalsIgnoreCase(RrsUtil.getLocalHostName())) {
            return true;
        }
        
        return false;
    }
    
    /**
     * getLocalHostName
     * @return returns name of local host
     */
    public static String getLocalHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch(java.net.UnknownHostException uhe) {
            logger.debug(uhe.getMessage());
            //handle exception
        }
        
        return null;
    }
    
    private static boolean isEmpty(String value){
        
        if (value == null || value.equals(""))
            return true;
        
        return false;
    }
    
    private boolean isNotEmpty(String value){
        
        if (value == null || value.equals(""))
            return false;
        
        return true;
    }
    
    
    
}
