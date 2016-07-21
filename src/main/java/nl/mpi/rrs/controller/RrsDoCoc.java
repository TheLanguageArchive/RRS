/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.rrs.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mpi.rrs.RrsConstants;
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
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
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

	RegisFileIO regisFileIO = (RegisFileIO) this.getServletContext().getAttribute(RrsConstants.REGIS_FILE_IO);
	if (regisFileIO == null) {
	    logger.error("RegisFileIO is NOT initialized during deploy.");
	    throw new RuntimeException("RegisFileIO is NOT initialized during deploy");
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

	boolean cocAgree = "ON".equals(request.getParameter("coc_agree"));
	if (cocAgree) {
	    regisFileIO.updateCocSigned(userInfo);
	}

	StringBuilder checkEmailLinkBase = new StringBuilder();
	String urlFields[] = request.getRequestURL().toString().split("/");

	for (int i = 0; i < urlFields.length - 1; i++) {
	    checkEmailLinkBase.append(urlFields[i]);
	    checkEmailLinkBase.append("/");
	}

	regisFileIO.removeOldRegistrationsFromFile(RrsConstants.REGISTRATION_EXPIRATION_DAYS);

	RrsRegistration rrsRegistration = new RrsRegistration();
	rrsRegistration.setUser(userInfo);
	rrsRegistration.setBaseUrl(checkEmailLinkBase.toString());

	logger.debug("checkEmailLinkBase: " + checkEmailLinkBase);

	String checkEmailLink = rrsRegistration.getCheckEmailLink();

	request.setAttribute("serverInfo", checkEmailLink);
	logger.debug("checkEmailLink: " + checkEmailLink);

	EmailBean emailer = new EmailBean();

        rrsRegistration.setEmailAddressCheckContent();

        emailer.setSubject((String) this.getServletContext().getAttribute(RrsConstants.CHECK_EMAIL_SUBJECT_ATTRIBUTE));

        emailer.setContent(rrsRegistration.getEmailAddressCheckContent());

        String corpmanEmail = (String) this.getServletContext().getAttribute(RrsConstants.EMAIL_ADDRESS_CORPMAN_ATTRIBUTE);
        String emailHost = (String) this.getServletContext().getAttribute(RrsConstants.SMTP_HOST_ATTRIBUTE);
        String userEmail = rrsRegistration.getUser().getEmail();

        emailer.setTo(userEmail);
        emailer.setCc(corpmanEmail);
        emailer.setFrom(corpmanEmail);
        emailer.setSmtpHost(emailHost);

        try {
            emailer.sendMessage();
            logger.info("Verification e-mail sent to " + userEmail);
        } catch (javax.mail.SendFailedException e) {
            ErrorRequest errorRequest = new ErrorRequest();

	String corpmanEmail = (String) this.getServletContext().getAttribute(RrsConstants.EMAIL_ADDRESS_CORPMAN_ATTRIBUTE);
	String emailHost = (String) this.getServletContext().getAttribute(RrsConstants.SMTP_HOST_ATTRIBUTE);
	String userEmail = rrsRegistration.getUser().getEmail();

	emailer.setTo(userEmail);
	emailer.setCc(corpmanEmail);
	emailer.setFrom(corpmanEmail);
	emailer.setSmtpHost(emailHost);

	try {
	    emailer.sendMessage();
	    logger.info("Verification e-mail sent to " + userEmail);
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

	    errorRequest.setErrorFormFieldLabel("Sending e-mail");
	    errorRequest.setErrorMessage(e.getMessage());
	    errorRequest.setErrorValue(emailHost);
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
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
