/*
 * RrsContextListener.java
 *
 * Created on February 5, 2007, 1:06 PM
 */

package nl.mpi.rrs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mpi.corpusstructure.CorpusStructureDBImpl;
import mpi.corpusstructure.NodeIdUtils;

/**
 *
 * @author kees
 * @version
 */
public class RrsContextListenerTester extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet RrsContextListener</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet RrsContextListener at " + request.getContextPath() + "</h1>");
        
        int nodeIdInt = 363998;
        String nodeId = NodeIdUtils.TONODEID(nodeIdInt);
        
        CorpusStructureDBImpl corpusDbConnection = (CorpusStructureDBImpl) getServletContext().getAttribute("corpusDbConnection");
        String nodeName = corpusDbConnection.getNode(nodeId).getName();
        
        out.println("Name of nodeid 363998 is: " + nodeName);
        
        Statement sql = null;
        Connection amsDbConnection = (Connection) getServletContext().getAttribute("amsDbConnection");
        
        try {         
            sql = amsDbConnection.createStatement();
            String sql_query = "select user_name,first_name,middle_name,last_name from users where user_name = 'kees'";
            
            ResultSet rs = sql.executeQuery(sql_query);
            while (rs.next()) {
                out.println("first_name: " + rs.getString("first_name"));
                
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        out.println("</body>");
        out.println("</html>");
        //
        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
