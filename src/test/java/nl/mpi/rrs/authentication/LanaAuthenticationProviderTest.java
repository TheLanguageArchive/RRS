package nl.mpi.rrs.authentication;

import nl.mpi.lat.auth.lana2.LanaAuthFilter;
import org.junit.Ignore;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class LanaAuthenticationProviderTest {

    private MockServletContext servletContext = new MockServletContext();
    private LanaAuthenticationProvider authProvider = new LanaAuthenticationProvider();
    private final static String ANONYMOUS_USERNAME = "anonymous";
    private final static String LOGGEDIN_USERNAME = "john@doe.net";

    @Test
    public void UserNotLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setRemoteUser(null);
        assertFalse(authProvider.isUserLoggedIn(request));
        request.setRemoteUser(LOGGEDIN_USERNAME);
        assertFalse(authProvider.isUserLoggedIn(request));
        request.setRemoteUser(ANONYMOUS_USERNAME);
        assertFalse(authProvider.isUserLoggedIn(request));
    }

    @Test
    @Ignore(value = "Some dependecy issue for the test(?)")
    public void UserLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setAttribute(LanaAuthFilter.ATTRIBUTE_UID, LOGGEDIN_USERNAME);
        assertTrue(authProvider.isUserLoggedIn(request));
        assertEquals(authProvider.getLoggedInUser(request), LOGGEDIN_USERNAME);
    }
}
