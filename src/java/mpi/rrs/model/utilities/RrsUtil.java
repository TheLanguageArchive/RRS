/*
 * RrsUtil.java
 *
 * Created on March 30, 2007, 12:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.model.utilities;

/**
 *
 * @author kees
 */
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class RrsUtil {
    static Logger logger = Logger.getLogger(RrsUtil.class);
    
    /** Creates a new instance of RrsUtil */
    public RrsUtil() {
    }
    
    /**
     * getLocalHostName
     * @return returns name of local host
     */
    public static String getLocalHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch(java.net.UnknownHostException uhe) {
            logger.error("RrsUtil:getLocalHostName", uhe);
            //handle exception
        }
        
        return null;
    }
    
    public static boolean isEmpty(String value){
        
        if (value == null || value.equals(""))
            return true;
        
        return false;
    }
    
    public static boolean isNotEmpty(String value){
        
        if (value == null || value.equals(""))
            return false;
        
        return true;
    }
    
}
