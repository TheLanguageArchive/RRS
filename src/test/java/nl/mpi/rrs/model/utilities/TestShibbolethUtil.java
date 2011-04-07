package nl.mpi.rrs.model.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class TestShibbolethUtil {

    private MockServletContext servletContext = new MockServletContext();
    private nl.mpi.rrs.model.utilities.ShibbolethUtil util = new ShibbolethUtil();

    private final static String ANONYMOUS_USERNAME="anonymous";
    private final static String LOGGEDIN_USERNAME="john@doe.net";

    @Test
    public void UserNotLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setRemoteUser(null);
        Assert.assertFalse(util.isUserLoggedIn(request));
        request.setRemoteUser("");
        Assert.assertFalse(util.isUserLoggedIn(request));
        request.setRemoteUser(ANONYMOUS_USERNAME);
        Assert.assertFalse(util.isUserLoggedIn(request));
    }

    @Test
    public void UserLoggedIn() {

        MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        request.setRemoteUser(LOGGEDIN_USERNAME);
        Assert.assertTrue(util.isUserLoggedIn(request));
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param shibbolethUtil the shibbolethUtil to set
     */
    public void setShibbolethUtil(ShibbolethUtil shibbolethUtil) {
        this.util = shibbolethUtil;
    }
}
