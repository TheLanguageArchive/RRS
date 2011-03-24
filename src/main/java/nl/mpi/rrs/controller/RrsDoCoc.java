/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.controller;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;
import nl.mpi.rrs.model.RrsRegistration;
import nl.mpi.rrs.model.email.EmailBean;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.registrations.RegisFileIO;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.utilities.RrsUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Verify that Dobes Code of Conduct has been signed
 * Send verification link to user
 * 
 * Point in registration workflow:
 * Show Dobes Code of Conduct -> 
 * User gets email with link to verify email address
 * 
 * @author kees
 */
public class RrsDoCoc extends HttpServlet {

    
    private static Log logger = LogFactory.getLog(RrsDoCoc.class);

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ErrorsRequest errorsRequest = new ErrorsRequest();
        String userName = request.getParameter("userName");

        request.setAttribute("userEmail", request.getParameter("userEmail"));
        request.setAttribute("userName", request.getParameter("userName"));
        request.setAttribute("userFirstName", request.getParameter("userFirstName"));
        request.setAttribute("userLastName", request.getParameter("userLastName"));

        RegisFileIO regisFileIO = (RegisFileIO) this.getServletContext().getAttribute("regisFileIO");
        if (regisFileIO == null) {
            logger.error("RegisFileIO is NOT initialized during deploy.");
            String rrsRegistrationFileName = this.getServletContext().getInitParameter("REGISTRATION_FILENAME");
            regisFileIO = new RegisFileIO(rrsRegistrationFileName);
        }

        RegistrationUser userInfo = regisFileIO.getRegistrationFromFile(userName);

        if (userInfo == null || RrsUtil.isEmpty(userInfo.getUserName())) {
            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Sign DObes Code of Conduct");
            errorRequest.setErrorMessage("Missing User Info");
            errorRequest.setErrorValue("Probably submit after complete registration");
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("MISSING_USER_INFO_IN_REGISTRATION_FILE");
            errorRequest.setErrorRecoverable(false);

            errorsRequest.addError(errorRequest);

            errorsRequest.setErrorsHtmlTable();

            String htmlErrorTable = errorsRequest.getErrorsHtmlTable();

            request.setAttribute("htmlErrorTable", htmlErrorTable);

            logger.error("User tries to submit after registration");

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorOther.jsp");
            view.forward(request, response);
            return;
        }

        regisFileIO.updateCocSigned(userInfo);

        String checkEmailLinkBase = "";
        String urlFields[] = request.getRequestURL().toString().split("/");

        for (int i = 0; i < urlFields.length - 1; i++) {
            checkEmailLinkBase += urlFields[i];
            checkEmailLinkBase += "/";
        }

        int daysOld = 30;
        int recordsRemoved = regisFileIO.removeOldRegistrationsFromFile(daysOld);

        RrsRegistration rrsRegistration = new RrsRegistration();
        rrsRegistration.setUser(userInfo);
        rrsRegistration.setBaseUrl(checkEmailLinkBase);

        logger.info("checkEmailLinkBase: " + checkEmailLinkBase);

        String checkEmailLink = rrsRegistration.getCheckEmailLink();

        request.setAttribute("serverInfo", checkEmailLink);
        logger.info("checkEmailLink: " + checkEmailLink);

        EmailBean emailer = new EmailBean();


        rrsRegistration.setEmailAddressCheckContent();

        emailer.setSubject("Max Planck Institute registration check");

        emailer.setContent(rrsRegistration.getEmailAddressCheckContent());

        String corpmanEmail = (String) this.getServletContext().getAttribute("emailAddressCorpman");
        String emailHost = (String) this.getServletContext().getAttribute("emailHost");
        String userEmail = rrsRegistration.getUser().getEmail();

        emailer.setTo(corpmanEmail);
        emailer.setCc(userEmail);
        emailer.setFrom(corpmanEmail);
        emailer.setSmtpHost(emailHost);

        try {
            emailer.sendMessage();
        } catch (javax.mail.SendFailedException e) {
            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Form field: Email");
            errorRequest.setErrorMessage("Invalid Email address");
            errorRequest.setErrorValue(userEmail);
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("INVALID_USER_EMAIL");
            errorRequest.setErrorRecoverable(true);

            errorsRequest.addError(errorRequest);

            errorsRequest.setErrorsHtmlTable();

            String htmlErrorTable = errorsRequest.getErrorsHtmlTable();

            request.setAttribute("htmlErrorTable", htmlErrorTable);

            logger.error("RrsServlet javax.mail.SendFailedException: can't send email", e);

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
            view.forward(request, response);
            return;

        } catch (java.lang.Exception e) {
            // catch all other possible email errors
            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Form field: Email");
            errorRequest.setErrorMessage("Invalid Email address (3)");
            errorRequest.setErrorValue(userEmail);
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("INVALID_USER_EMAIL");
            errorRequest.setErrorRecoverable(true);

            errorsRequest.addError(errorRequest);

            errorsRequest.setErrorsHtmlTable();

            String htmlErrorTable = errorsRequest.getErrorsHtmlTable();

            request.setAttribute("htmlErrorTable", htmlErrorTable);

            logger.error("RrsServlet java.lang.Exception: can't send email", e);

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
            view.forward(request, response);
            return;
        /*
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorUnknown.jsp");
        view.forward(request, response);   
        return;
         */

        }

        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/dobesCocReply.jsp");
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
        return "Short description";
    }
    // </editor-fold>
}
