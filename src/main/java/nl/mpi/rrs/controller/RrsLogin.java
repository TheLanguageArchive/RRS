package nl.mpi.rrs.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class RrsLogin extends HttpServlet {

    private final static Log logger = LogFactory.getLog(RrsLogin.class);

    public enum RedirectLocation {

        RrsRegistration,
        RrsIndex
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("RrsLogin");
        String redirectString = request.getParameter("redirect");
        if (redirectString != null && !redirectString.isEmpty()) {
            try {
                // check against allowed redirect locations
                RedirectLocation location = RedirectLocation.valueOf(redirectString);
                // no exception thrown, so valid redirect location
                logger.info("Redirecting to " + location.toString());
                response.sendRedirect(location.toString());
                return;
            } catch (IllegalArgumentException ex) {
                logger.warn("Illegal redirect attempt: " + redirectString);
            }
        }
        PrintWriter writer = response.getWriter();
        writer.append("Illegal or invalid redirect");
        writer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
