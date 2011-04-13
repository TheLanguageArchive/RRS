package nl.mpi.rrs.model.utilities;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Currently tests whether util responds properly to the remote user property.
 * TODO: Mocking Shibboleth would be very nice to have, but is cumbersome (if possible)
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TestShibbolethAuthenticationProvider {

    private MockServletContext servletContext = new MockServletContext();
    private nl.mpi.rrs.model.utilities.ShibbolethAuthenticationProvider util = new ShibbolethAuthenticationProvider();

    private final static String ANONYMOUS_USERNAME="anonymous";
    private final static String LOGGEDIN_USERNAME="john@doe.net";

    @Test
    public void UserNotLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setRemoteUser(null);
        assertFalse(util.isUserLoggedIn(request));
        request.setRemoteUser("");
        assertFalse(util.isUserLoggedIn(request));
        request.setRemoteUser(ANONYMOUS_USERNAME);
        assertFalse(util.isUserLoggedIn(request));
    }

    @Test
    public void UserLoggedIn() {

        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setRemoteUser(LOGGEDIN_USERNAME);
        assertTrue(util.isUserLoggedIn(request));
    }
}
