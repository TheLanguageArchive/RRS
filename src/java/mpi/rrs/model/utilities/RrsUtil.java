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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mpi.rrs.model.registrations.RegisFileIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Logger;

public class RrsUtil {
    private static Log _log = LogFactory.getLog(RegisFileIO.class);
    //static Logger logger = Logger.getLogger(RrsUtil.class);
    
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
            _log.error("RrsUtil:getLocalHostName", uhe);
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
    
    /**
     * create Date object with specified format for today
     * @param format for date
     * @return returns Date object with specified format for today
     */
    public static String todayFormat(String format) {
        if (format == null) {
            format = "dd-MMM-yyyy HH':'mm':'ss";
        }
        
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        DateFormat df = DateFormat.getDateInstance();
        String date = formatter.format(today);
        
        return date;
    }
    
    /**
     * Creates a String object with todays date
     * @return returns todays date
     */
    public static String todayDateAsString() {
        Date today = new Date();
        DateFormat df = DateFormat.getDateInstance();
        String date = df.format(today);
        
        return date;
    }
    
    /**
     * Creates a Date object with todays date
     * @return returns todays date
     */
    public static Date todayDate() {
        Date today = new Date();
        
        return today;
    }
    
     /**
     * dateDiffFromToday -- compute the difference in days between today and fromDate.
     * @param fromDate date to compute difference from today
     * @return days between today and fromDate
     */
    public static int dateDiffFromToday(Date fromDate) {
        
        /** Today's date */
        Date today = new Date( );
        
        // Get msec from each, and subtract.
        long diff = today.getTime( ) - fromDate.getTime( );
        int days = (int) Math.floor(diff / (1000*60*60*24));
        
        return days;
    }
    
    /**
     * execCommand executes a system command
     * @param command sfind command
     * @return true if successful
     */
    public static boolean execCommand(String command) {
        try {
            
            _log.info("Executing: " + command);
            
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            
            try {
                
                int exitCode = p.waitFor();
                
                if (exitCode == 0) {
                    return true;
                } else {
                    return false;
                }
                
            } catch (java.lang.InterruptedException i) {
                _log.error("InterruptedException: " + i.getMessage());
                return false;
            }
        } catch (IOException e) {
            _log.error("IOException:" + e.getMessage());
            return false;
        }
        
    }
    
    
}
