package nl.mpi.rrs.model.utilities;

import de.mpg.aai.shhaa.context.AuthenticationContext;
import de.mpg.aai.shhaa.context.AuthenticationContextHolder;
import javax.servlet.http.HttpServletRequest;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.user.shibboleth.SihbbolethRegistrationUser;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ShibbolethUtil implements AuthenticationUtility {

//    private static Log _log = LogFactory.getLog(ShibbolethUtil.class);
    public final static String SHIB_ANONYMOUS_USER = "anonymous";

    public boolean isUserLoggedIn(HttpServletRequest request) {
        String user = request.getRemoteUser();
        return user != null
                && !user.isEmpty()
                && !SHIB_ANONYMOUS_USER.equals(user);
    }

    public String getLoggedInUser(HttpServletRequest request) {
        if (isUserLoggedIn(request)) {
            return request.getRemoteUser();
        } else {
            return null;
        }
    }

    public RegistrationUser createRegistrationUser(HttpServletRequest request) {
        AuthenticationContext context = AuthenticationContextHolder.get(request);
        SihbbolethRegistrationUser shibRegUser = new SihbbolethRegistrationUser(context);
        shibRegUser.setAttributesFromShibbolethContext();
        return shibRegUser;
    }
}
