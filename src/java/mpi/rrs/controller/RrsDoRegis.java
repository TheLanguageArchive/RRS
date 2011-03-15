/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpi.rrs.controller;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import mpi.rrs.model.RrsRegistration;
import mpi.rrs.model.errors.ErrorRequest;
import mpi.rrs.model.errors.ErrorsRequest;

import mpi.rrs.model.registrations.RegisFileIO;
import mpi.rrs.model.user.RegistrationUser;
import mpi.rrs.model.user.UserGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Verify user registration form
 * Show Dobes Code of Conduct license
 * 
 * Point in registration workflow:
 * User registration -> 
 * Show Dobes Code of Conduct
 *
 * 
 * @author kees
 */
public class RrsDoRegis extends HttpServlet {

    
    private static Log logger = LogFactory.getLog(RrsDoRegis.class);
    RrsRegistration rrsRegistration = new RrsRegistration();
    ErrorsRequest errorsRequest = new ErrorsRequest();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ams2 : using defaults : user-data provider and authentication service 
        String rrsRegistrationFileName = this.getServletContext().getInitParameter("REGISTRATION_FILENAME");


        RegisFileIO regisFileIO = (RegisFileIO) this.getServletContext().getAttribute("regisFileIO");
        if (regisFileIO == null) {
            logger.error("RegisFileIO is NOT initialized during deploy.");

            regisFileIO = new RegisFileIO(rrsRegistrationFileName);
        }

        UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute("ams2DbConnection");

        RegistrationUser userInfo = new RegistrationUser();
        userInfo.setUserName(request.getParameter("paramUserNewUserName"));
        userInfo.setFirstName(request.getParameter("paramUserNewFirstName"));
        userInfo.setLastName(request.getParameter("paramUserNewLastName"));
        userInfo.setEmail(request.getParameter("paramUserNewEmail"));
        userInfo.setOrganization(request.getParameter("paramUserNewOrganization"));
        userInfo.setPassword(request.getParameter("paramUserNewPassword_1"));
        userInfo.setCreation_ts();

        request.setAttribute("paramUserNewUserName", userInfo.getUserName());
        request.setAttribute("paramUserNewFirstName", userInfo.getFirstName());
        request.setAttribute("paramUserNewLastName", userInfo.getLastName());
        request.setAttribute("paramUserNewEmail", userInfo.getEmail());
        request.setAttribute("paramUserNewOrganization", userInfo.getOrganization());
        request.setAttribute("paramUserNewPassword_1", request.getParameter("paramUserNewPassword_1"));
        request.setAttribute("paramUserNewPassword_2", request.getParameter("paramUserNewPassword_2"));

        // ug = (Ams2UserGenerator) this.getServletContext().getAttribute("ams2DbConnection");

        String userId = userInfo.getUserName();

        if (ug.isExistingUserName(userId)) {

            request.setAttribute("rrsRegisErrorMessage", "This User ID is already taken, please use a different one!");

            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Form field: UserID");
            errorRequest.setErrorMessage("This User ID is already taken, please use a different one!");
            errorRequest.setErrorValue("UserID: " + userId);
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("EXISTING_USER_ID");
            errorRequest.setErrorRecoverable(true);

            errorsRequest.addError(errorRequest);

            logger.debug("User ID is already taken: " + userId);
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
            view.forward(request, response);

        } else if (regisFileIO.isRegistrationInFile(userInfo)) {
            request.setAttribute("rrsRegisErrorMessage", "This User ID is already reserved, please use a different one!");

            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Form field: UserID");
            errorRequest.setErrorMessage("This User ID is already reserved, please use a different one!");
            errorRequest.setErrorValue("UserID: " + userId);
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("EXISTING_USER_ID");
            errorRequest.setErrorRecoverable(true);

            errorsRequest.addError(errorRequest);

            logger.debug("User ID is already reserved: " + userId);
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
            view.forward(request, response);

        } else {
            request.setAttribute("user", userInfo);

            request.setAttribute("rrsRegisErrorMessage", "");

            boolean success = regisFileIO.writeRegistrationToFile(userInfo);

            if (!success) {
                request.setAttribute("rrsRegisErrorMessage", "Error adding  user to registration file: " + rrsRegistrationFileName);
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
                view.forward(request, response);
            } else {
                String urlRrsDobesCoc = this.getServletContext().getInitParameter("DOBES_COC_LINK");

                if (urlRrsDobesCoc.contentEquals("default")) {
                    urlRrsDobesCoc = request.getContextPath() + "/dobes_coc_v2.html";
                    request.setAttribute("urlRrsDobesCoc", urlRrsDobesCoc);
                }

                request.setAttribute("urlRrsDobesCoc", urlRrsDobesCoc);
                
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/dobesCoc.jsp");
                view.forward(request, response);
            }

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
        return "Short description";
    }
    // </editor-fold>
}
