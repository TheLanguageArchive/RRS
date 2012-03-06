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

import nl.mpi.rrs.model.registrations.RegisFileIO;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.user.UserGenerator;
import nl.mpi.rrs.authentication.AuthenticationProvider;

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
	RegisFileIO regisFileIO = initRegisFileIO();
	RegistrationUser userInfo = initUserInfo(request, errorsRequest);

	request.setAttribute("federated", authenticationProvider.isFederated());
	request.setAttribute("newInternalUser", this.getServletContext().getAttribute(RrsConstants.ALLOW_NEW_INTERNAL_USERS_ATTRIBUTE));
	request.setAttribute("corpmanEmail", this.getServletContext().getAttribute(RrsConstants.EMAIL_ADDRESS_CORPMAN_ATTRIBUTE));

	if (userInfo.validate()) {
	    RequestDispatcher view = doRegistration(request, response, regisFileIO, userInfo, errorsRequest);
	    view.forward(request, response);
	} else {
	    request.setAttribute("rrsRegisErrorMessage", "One or more values are incorrect or missing from the registration form");

	    ErrorRequest errorRequest = new ErrorRequest();

	    errorRequest.setErrorFormFieldLabel("");
	    errorRequest.setErrorMessage("One or more values are missing from the registration form");
	    errorRequest.setErrorValue("");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("FORM_DATA_INVALID");
	    errorRequest.setErrorRecoverable(true);

	    errorsRequest.addError(errorRequest);

	    logger.debug("UserInfo invalid");
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
	    view.forward(request, response);
	}
    }

    private RegisFileIO initRegisFileIO() {
	// ams2 : using defaults : user-data provider and authentication service
	RegisFileIO regisFileIO = (RegisFileIO) this.getServletContext().getAttribute(RrsConstants.REGIS_FILE_IO);
	if (regisFileIO == null) {
	    logger.fatal("RegisFileIO is NOT initialized during deploy.");
	    throw new RuntimeException("RegisFileIO is NOT initialized during deploy");
	}
	return regisFileIO;
    }

    private RegistrationUser initUserInfo(HttpServletRequest request, ErrorsRequest errorsRequest) {
	boolean allowNewInternalUser = (Boolean) getServletContext().getAttribute(RrsConstants.ALLOW_NEW_INTERNAL_USERS_ATTRIBUTE);

	RegistrationUser userInfo = new RegistrationUser();
	if (authenticationProvider.isUserLoggedIn(request)) {
	    String uid = authenticationProvider.getLoggedInUser(request);
	    userInfo.setUserName(uid);
	    request.setAttribute("uid", uid);
	} else {
	    if (allowNewInternalUser) {
		userInfo.setUserName(request.getParameter("paramUserNewUserName"));
	    } else {
		logger.error("Illegal request: unauthenticated user registration requested but new internal users not allowed!");
		userInfo.setUserName(null);

		ErrorRequest errorRequest = new ErrorRequest();

		errorRequest.setErrorFormFieldLabel("");
		errorRequest.setErrorMessage("Unauthenticated user registration requested but new internal users not allowed");
		errorRequest.setErrorValue("");
		errorRequest.setErrorException(null);
		errorRequest.setErrorType("ILLEGAL_REQUEST");
		errorRequest.setErrorRecoverable(true);

		errorsRequest.addError(errorRequest);

		return userInfo;
	    }
	}
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
	return userInfo;
    }

    private RequestDispatcher doRegistration(HttpServletRequest request, HttpServletResponse response, RegisFileIO regisFileIO, RegistrationUser userInfo, ErrorsRequest errorsRequest) throws IOException, ServletException {
	String userId = userInfo.getUserName();

	UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE);
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

	    logger.info("User ID is already taken: " + userId);
	    return request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
	} else {
	    if (regisFileIO.isRegistrationInFile(userInfo)) {
		request.setAttribute("rrsRegisErrorMessage", "This User ID is already reserved, please use a different one!");

		ErrorRequest errorRequest = new ErrorRequest();

		errorRequest.setErrorFormFieldLabel("Form field: UserID");
		errorRequest.setErrorMessage("This User ID is already reserved, please use a different one!");
		errorRequest.setErrorValue("UserID: " + userId);
		errorRequest.setErrorException(null);
		errorRequest.setErrorType("EXISTING_USER_ID");
		errorRequest.setErrorRecoverable(true);

		errorsRequest.addError(errorRequest);

		logger.info("User ID is already reserved: " + userId);
		return request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
	    } else {
		request.setAttribute("user", userInfo);

		request.setAttribute("rrsRegisErrorMessage", "");

		boolean success = regisFileIO.writeRegistrationToFile(userInfo);

		if (!success) {
		    logger.error("Error adding  user to registration file: " + regisFileIO.getRegistrationFilename());
		    request.setAttribute("rrsRegisErrorMessage", "Error adding  user to registration file: " + regisFileIO.getRegistrationFilename());
		    return request.getRequestDispatcher("/WEB-INF/view/page/regis2.jsp");
		} else {
		    String urlRrsDobesCoc = (String) this.getServletContext().getAttribute(RrsConstants.DOBES_COC_LINK_ATTRIBUTE);

		    if (urlRrsDobesCoc.contentEquals("default")) {
			urlRrsDobesCoc = request.getContextPath() + "/dobes_coc_v2.html";
			request.setAttribute("urlRrsDobesCoc", urlRrsDobesCoc);
		    } else if (urlRrsDobesCoc.contentEquals("none")) {
			request.setAttribute("urlRrsDobesCoc", null);
		    } else {
			request.setAttribute("urlRrsDobesCoc", urlRrsDobesCoc);
		    }
		    return request.getRequestDispatcher("/WEB-INF/view/page/dobesCoc.jsp");
		}
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
