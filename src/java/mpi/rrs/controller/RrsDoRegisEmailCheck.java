/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpi.rrs.controller;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;
import mpi.rrs.model.RrsRegistration;
import mpi.rrs.model.ams.AmsLicense;
import mpi.rrs.model.email.EmailBean;
import mpi.rrs.model.errors.ErrorRequest;
import mpi.rrs.model.errors.ErrorsRequest;
import mpi.rrs.model.registrations.RegisFileIO;
import mpi.rrs.model.user.RegistrationUser;
import mpi.rrs.model.user.UserGenerator2;
import nl.mpi.lat.fabric.NodeID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kees
 */
public class RrsDoRegisEmailCheck extends HttpServlet {

    
    private static Log logger = LogFactory.getLog(RrsDoRegisEmailCheck.class);

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ErrorsRequest errorsRequest = new ErrorsRequest();

        RegisFileIO regisFileIO = (RegisFileIO) this.getServletContext().getAttribute("regisFileIO");

        String userName = request.getParameter("RrsRegisUserName");
        String userRegisIdStr = request.getParameter("RrsRegisId");

        int userRegisId = new Integer(userRegisIdStr).intValue();

        request.setAttribute("RrsRegisUserName", userName);
        request.setAttribute("RrsRegisId", userRegisId);

        RegistrationUser user = new RegistrationUser();
        user.setUserName(userName);

        if (regisFileIO.isRegistrationInFile(user)) {
            RrsRegistration rrsRegistration = new RrsRegistration();
            RegistrationUser userInfo = regisFileIO.getRegistrationFromFile(userName);
            rrsRegistration.setUser(userInfo);
            request.setAttribute("userFirstName", userInfo.getFirstName());
            request.setAttribute("userLastName", userInfo.getLastName());
            request.setAttribute("userEmail", userInfo.getEmail());

            if (rrsRegistration.getRegisId() == userRegisId) {
                ErrorsRequest errorsRequestEmail = new ErrorsRequest();
                EmailBean emailer = new EmailBean();

                rrsRegistration.setEmailAccountDetailsContent();

                emailer.setSubject("Max Planck Institute registration");

                emailer.setContent(rrsRegistration.getEmailAccountDetailsContent());

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

                    errorsRequestEmail.addError(errorRequest);

                    errorsRequestEmail.setErrorsHtmlTable();

                    String htmlErrorTable = errorsRequestEmail.getErrorsHtmlTable();

                    request.setAttribute("htmlErrorTable", htmlErrorTable);

                    logger.error("Unable to send account details for user " + userName + " with email address " + userEmail);
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

                    errorsRequestEmail.addError(errorRequest);

                    errorsRequestEmail.setErrorsHtmlTable();

                    String htmlErrorTable = errorsRequestEmail.getErrorsHtmlTable();

                    request.setAttribute("htmlErrorTable", htmlErrorTable);

                    logger.error("Unable to send account details for user " + userName + " with email address " + userEmail);
                    logger.error("java.lang.Exception: can't send email", e);

                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
                    view.forward(request, response);
                    return;
                }

                UserGenerator2 ug = (UserGenerator2) this.getServletContext().getAttribute("ams2DbConnection");

                boolean success = ug.addNewUser(userInfo);
                if (success) {
                    logger.info("User: " + userName + " successfuly added to AMS2 DB.");

                    success = regisFileIO.removeRegistrationFromFile(userInfo);
                    if (success) {
                        logger.info("User: " + userName + " successfuly removed from registration file.");
                    } else {
                        logger.error("User: " + userName + " can't be removed from registration file.");
                    }

                    /*
                    logger.info("users in group registered: \n");
                    String members = ug.getMembersOfGroup("registered");
                    logger.info(members);

                    if (!ug.isMemberOfGroup(userInfo.getUserName(), "registered")) {
                        success = ug.addMemberToGroup(userInfo.getUserName(), "registered");
                        if (success) {
                            logger.info("User: " + userInfo.getUserName() + " has been successfuly added to group: registered");
                        } else {
                            logger.info("User: " + userInfo.getUserName() + " has not been successfuly added to group: registered");
                        }
                    } else {
                        logger.info("User: " + userInfo.getUserName() + " is already member of group: registered");
                    }
                     */

                    //AmsServices services = new AmsServices();
                    AmsLicense al = new AmsLicense();
                    logger.info(al.getLicenseInfo(userInfo.getUserName(), null));

                    NodeID targetNodeID = null;
                    
                    String dobesCodeOfConductLicenseName = this.getServletContext().getInitParameter("DOBES_COC_LICENSE_NAME");
                    logger.info("dobesCodeOfConductLicenseName: " + dobesCodeOfConductLicenseName);
                    al.acceptLicenseInfo(userInfo.getUserName(), targetNodeID, dobesCodeOfConductLicenseName);
                    
                    logger.info("*** END OF REGISTRATION for user: " + userInfo.getUserName());
                    logger.info("");
                    
                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/emailCheckOk.jsp");
                    view.forward(request, response);
                    return;

                } else {
                    logger.error("Can't add user: " + userName + " to AMS2 DB.");
                }

            } else {
                ErrorRequest errorRequest = new ErrorRequest();

                errorRequest.setErrorFormFieldLabel("Registration Id");
                errorRequest.setErrorMessage("Wrong Id");
                errorRequest.setErrorValue(userRegisIdStr);
                errorRequest.setErrorException(null);
                errorRequest.setErrorType("INVALID_REGIS_ID");
                errorRequest.setErrorRecoverable(false);

                errorsRequest.addError(errorRequest);
                errorsRequest.setErrorRecoverable(false);
            }
        } else {
            ErrorRequest errorRequest = new ErrorRequest();

            errorRequest.setErrorFormFieldLabel("Registration username");
            errorRequest.setErrorMessage("Unknown");
            errorRequest.setErrorValue(userName);
            errorRequest.setErrorException(null);
            errorRequest.setErrorType("INVALID_USER_NAME");
            errorRequest.setErrorRecoverable(false);

            errorsRequest.addError(errorRequest);
            errorsRequest.setErrorRecoverable(false);
        }

        if (errorsRequest.getSize() > 0) {

            errorsRequest.setErrorsHtmlTable();

            String htmlErrorTable = errorsRequest.getErrorsHtmlTable();

            request.setAttribute("htmlErrorTable", htmlErrorTable);

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorEmailCheck.jsp");
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
        return "Short description";
    }
    // </editor-fold>
}
