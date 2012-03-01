package nl.mpi.rrs.authentication;

import javax.servlet.http.HttpServletRequest;
import nl.mpi.lat.auth.lana2.LanaAuthFilter;
import nl.mpi.rrs.model.user.RegistrationUser;

/**
 * Lana2 implementation of AuthenticationProvider
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class LanaAuthenticationProvider implements AuthenticationProvider {

    public String getLoggedInUser(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(LanaAuthFilter.ATTRIBUTE_UID);
    }

    public boolean isUserLoggedIn(HttpServletRequest request) {
        return null != request.getSession().getAttribute(LanaAuthFilter.ATTRIBUTE_UID);
    }

    public String getIdentityProviderId(HttpServletRequest request) {
        return "Lana2";
    }

    public RegistrationUser createRegistrationUser(HttpServletRequest request) {
        return new RegistrationUser();
    }

    public boolean isFederated() {
        return false;
    }
}
