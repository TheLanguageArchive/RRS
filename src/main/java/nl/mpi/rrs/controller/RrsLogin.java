package nl.mpi.rrs.controller;

import java.io.IOException;
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

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("RrsLogin");
        String redirectString = request.getParameter("redirect");
        if (redirectString != null && !redirectString.isEmpty()) {
            logger.info("Redirecting to " + redirectString);
            response.sendRedirect(redirectString);
        }
        // TODO
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
