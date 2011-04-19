/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.controller;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import nl.mpi.rrs.RrsConstants;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.user.UserGenerator;
import nl.mpi.rrs.authentication.AuthenticationProvider;

/**
 * Show User Registration Form
 *
 * @author kees
 */
public class RrsRegis extends HttpServlet {

    private AuthenticationProvider authenticationProvider;

    @Override
    public void init() throws ServletException {
        super.init();
        // Get authentication utility from servlet context. It is put there through
        // spring configuration in spring-rrs-auth(-test).xml
        authenticationProvider = (AuthenticationProvider) getServletContext().getAttribute(RrsConstants.AUTHENTICATION_PROVIDER_ATTRIBUTE);
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ErrorsRequest errorsRequest = new ErrorsRequest();
        checkCurrentUser(request, response, errorsRequest);
        dispatchServlet(request, response, errorsRequest);
    }

    /**
     * Checks the current user (if there is one), and sets response properties accordingly
     * @param request Servlet request
     * @param response Servlet response
     */
    private void checkCurrentUser(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest) {
        if (authenticationProvider.isUserLoggedIn(request)) {
            String uid = authenticationProvider.getLoggedInUser(request);
            // User already logged in
            UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE);
            if (ug.isExistingUserName(uid)) {
                // User already logged in and registered. Should not register again
                ErrorRequest errorRequest = new ErrorRequest();
                errorRequest.setErrorFormFieldLabel("Username");
                errorRequest.setErrorMessage("User is already registered in database");
                errorRequest.setErrorValue(uid);
                errorRequest.setErrorException(null);
                errorRequest.setErrorType("USER_REREGISTER");
                errorRequest.setErrorRecoverable(true);
                errorsRequest.setErrorFromBrowser(true);
                errorsRequest.addError(errorRequest);
                return;
            } else {
                // Try to pre-fill the form

                request.setAttribute("uid", uid);
                request.setAttribute("federated", authenticationProvider.isFederated());

                RegistrationUser user = authenticationProvider.createRegistrationUser(request);
                request.setAttribute("paramUserNewFirstName", user.getFirstName());
                request.setAttribute("paramUserNewLastName", user.getLastName());
                request.setAttribute("paramUserNewEmail", user.getEmail());
                request.setAttribute("paramUserNewOrganization", user.getOrganization());
            }
        }
    }

    /** 
     * View registration page
     * @param request servlet request
     * @param response servlet response
     */
    public void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest) throws ServletException, IOException {
        if (errorsRequest.getSize() <= 0) {
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
            view.forward(request, response);
        } else {
            errorsRequest.setErrorsHtmlTable();
            String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
            request.setAttribute("htmlErrorTable", htmlErrorTable);

            RequestDispatcher view = request.getRequestDispatcher(
                    errorsRequest.isErrorFromBrowser()
                    ? "/WEB-INF/view/error/errorBrowser.jsp"
                    : "/WEB-INF/view/error/errorUnknown.jsp");
            view.forward(request, response);
        }
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
