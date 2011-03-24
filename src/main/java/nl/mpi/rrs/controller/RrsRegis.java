/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.controller;

import de.mpg.aai.shhaa.context.AuthenticationContext;
import de.mpg.aai.shhaa.context.AuthenticationContextHolder;
import de.mpg.aai.shhaa.model.AuthAttributes;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import nl.mpi.rrs.model.user.UserGenerator;
//import javax.servlet.RequestDispatcher;

/**
 * Show User Registration Form
 *
 * @author kees
 */
public class RrsRegis extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");

        if (!checkCurrentUser(request, response)) {
            dispatchServlet(request, response);
        }
    }

    private boolean checkCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        String uidFromShib = request.getRemoteUser();
        if (uidFromShib != null) {
            // User already logged in
            UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute("ams2DbConnection");
            if(false){ // (ug.isExistingUserName(uidFromShib)){
                // User already logged in and registered
                return true;
            } else{
                // Try to pre-fill the form
                AuthenticationContext context =  AuthenticationContextHolder.get(request);
                AuthAttributes attributes = context.getAuthPrincipal().getAttribues();
                for(String id:attributes.getIDs()){
                    request.setAttribute("att_"+id, attributes.get(id));
                }
            }
        }
        return false;
    }

    /** 
     * View registration page
     * @param request servlet request
     * @param response servlet response
     */
    public void dispatchServlet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
        view.forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Resource Request Registration";
    }
    // </editor-fold>
}
