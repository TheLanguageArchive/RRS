/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.mpi.rrs.model.utilities;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ShibbolethUtil {
    private static Log _log = LogFactory.getLog(ShibbolethUtil.class);

    public final static String SHIB_ANONYMOUS_USER = "anonymous";

    public static boolean isUserLoggedIn(HttpServletRequest request){
        String user = request.getRemoteUser();
        return user != null
                && !user.isEmpty()
                && !SHIB_ANONYMOUS_USER.equals(user);
    }

    public static String getLoggedInUser(HttpServletRequest request){
        if(isUserLoggedIn(request)){
            return request.getRemoteUser();
        } else{
            return null;
        }
    }
}
