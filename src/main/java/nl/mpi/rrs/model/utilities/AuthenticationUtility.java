package nl.mpi.rrs.model.utilities;

import javax.servlet.http.HttpServletRequest;
import nl.mpi.rrs.model.user.RegistrationUser;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public interface AuthenticationUtility {

    /**
     * Creates a registration user and attempts to set properties from the currently logged in user
     * @param request
     * @return Registration user with properties set where applicable
     */
    RegistrationUser createRegistrationUser(HttpServletRequest request);

    /**
     * @param request Current HttpServletRequest
     * @return Identity (username) of the user currently logged in
     */
    String getLoggedInUser(HttpServletRequest request);

    /**
     * @param request Current HttpServletRequest
     * @return Whether an authenticated user is currently logged in
     */
    boolean isUserLoggedIn(HttpServletRequest request);

}
