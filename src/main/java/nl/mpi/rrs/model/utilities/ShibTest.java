package nl.mpi.rrs.model.utilities;

import de.mpg.aai.shhaa.context.AuthenticationContext;
import de.mpg.aai.shhaa.context.AuthenticationContextHolder;
import de.mpg.aai.shhaa.model.AuthAttributes;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class ShibTest extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuthenticationContext context = AuthenticationContextHolder.get(request);
        AuthAttributes attributes = context.getAuthPrincipal().getAttribues();
        HashMap<String, Object> attributesMap = new HashMap<String, Object>(attributes.size());
        for (String id : attributes.getIDs()) {
            attributesMap.put(id, attributes.get(id).getValue());
        }
        request.setAttribute("attributes", attributesMap);
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/shibTest.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
