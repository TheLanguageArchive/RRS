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
import nl.mpi.rrs.model.RrsRegistration;
import nl.mpi.rrs.model.ams.AmsLicense;
import nl.mpi.rrs.model.email.EmailBean;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.registrations.RegisFileIO;
import nl.mpi.rrs.model.user.RegistrationUser;
import nl.mpi.rrs.model.user.UserGenerator;
import nl.mpi.lat.fabric.NodeID;
import nl.mpi.rrs.RrsConstants;
import nl.mpi.rrs.authentication.AuthenticationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * saves user to ams DB
 *
 * Point in registration workflow:
 * User registration ->
 * Show Dobes Code of Conduct ->
 * User gets email with link to verify email address ->
 * User clicks on link
 *
 * @author kees
 */
public class RrsDoRegisEmailCheck extends HttpServlet {

    private static Log logger = LogFactory.getLog(RrsDoRegisEmailCheck.class);
    private RegisFileIO regisFileIO;
    private String amsInterfaceLink;
    private AuthenticationProvider authenticationProvider;
    private String archiveUsersIdpName = null;

    @Override
    public void init() throws ServletException {
	regisFileIO = (RegisFileIO) this.getServletContext().getAttribute(RrsConstants.REGIS_FILE_IO);
	amsInterfaceLink = (String) this.getServletContext().getAttribute(RrsConstants.AMS_INTERFACE_LINK);
	authenticationProvider = (AuthenticationProvider) getServletContext().getAttribute(RrsConstants.AUTHENTICATION_PROVIDER_ATTRIBUTE);
	archiveUsersIdpName = (String) this.getServletContext().getAttribute(RrsConstants.ARCHIVE_USERS_IDP_NAME);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String userName = request.getParameter("RrsRegisUserName");
	String userRegisIdStr = request.getParameter("RrsRegisId");

	int userRegisId = new Integer(userRegisIdStr).intValue();

	request.setAttribute("RrsRegisUserName", userName);
	request.setAttribute("RrsRegisId", userRegisId);

	ErrorsRequest errorsRequest = new ErrorsRequest();
	if (processNewUserIfRegistered(request, response, errorsRequest, userName, userRegisId)) {
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/emailCheckOk.jsp");
	    if (archiveUsersIdpName != null && archiveUsersIdpName.length() > 0
		    && authenticationProvider.isFederated()
		    && !authenticationProvider.isUserLoggedIn(request)) {
		request.setAttribute("idpName", archiveUsersIdpName);
	    }
	    view.forward(request, response);
	} else {
	    // Registration failed. Show errors table
	    errorsRequest.setErrorsHtmlTable();

	    String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
	    request.setAttribute("htmlErrorTable", htmlErrorTable);

	    String urlRrsRegistration = request.getContextPath() + "/RrsRegistration";
	    request.setAttribute("urlRrsRegistration", urlRrsRegistration);

	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorEmailCheck.jsp");
	    view.forward(request, response);
	}
    }

    private boolean processNewUserIfRegistered(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, String userName, int userRegisId) throws IOException, ServletException {
	RegistrationUser user = new RegistrationUser();
	user.setUserName(userName);
	if (regisFileIO.isRegistrationInFile(user)) {
	    return processNewUserIfValid(request, response, errorsRequest, userName, userRegisId);
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
	return false;
    }

    private boolean processNewUserIfValid(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, String userName, int userRegisId) throws ServletException, IOException {
	RrsRegistration rrsRegistration = new RrsRegistration();
	RegistrationUser userInfo = regisFileIO.getRegistrationFromFile(userName);
	rrsRegistration.setUser(userInfo);
	rrsRegistration.setAmsInterfaceLink(amsInterfaceLink);
	rrsRegistration.setArchiveUsersIdpName(archiveUsersIdpName);
	rrsRegistration.setFederatedUser(authenticationProvider.isFederated() && authenticationProvider.isUserLoggedIn(request));
	request.setAttribute("userFirstName", userInfo.getFirstName());
	request.setAttribute("userLastName", userInfo.getLastName());
	request.setAttribute("userEmail", userInfo.getEmail());
	if (rrsRegistration.getRegisId() == userRegisId) {
	    return processNewUser(request, response, errorsRequest, rrsRegistration, userInfo);
	} else {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Registration Id");
	    errorRequest.setErrorMessage("Wrong Id");
	    errorRequest.setErrorValue(Integer.toString(userRegisId));
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("INVALID_REGIS_ID");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorRecoverable(false);
	}
	return false;
    }

    private boolean processNewUser(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, RrsRegistration rrsRegistration, RegistrationUser userInfo) throws IOException, ServletException {
	String userName = userInfo.getUserName();

	if (sendRegistrationEmail(request, response, rrsRegistration)) {
	    if (addUser(request, response, errorsRequest, userInfo)) {
		logger.info("User: " + userName + " successfuly added to AMS2 DB.");
		if (regisFileIO.removeRegistrationFromFile(userInfo)) {
		    logger.info("User: " + userName + " successfuly removed from registration file.");
		} else {
		    logger.error("User: " + userName + " can't be removed from registration file.");
		}

		if (rrsRegistration.getUser().isDobesCocSigned()) {
		    return acceptLicenseForUser(request, response, errorsRequest, userInfo, rrsRegistration.getRegisId());
		} else {
		    return true;
		}
	    } else {
		ErrorRequest errorRequest = new ErrorRequest();
		errorRequest.setErrorFormFieldLabel("Add user");
		errorRequest.setErrorMessage("Can't add user");
		errorRequest.setErrorValue(Integer.toString(rrsRegistration.getRegisId()));
		errorRequest.setErrorException(null);
		errorRequest.setErrorType("CANNOT_ADD_USER");
		errorRequest.setErrorRecoverable(false);
		errorsRequest.addError(errorRequest);
		errorsRequest.setErrorRecoverable(false);
		logger.error("Can't add user: " + userName + " to AMS2 DB.");
	    }
	}
	return false;
    }

    private boolean acceptLicenseForUser(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, RegistrationUser userInfo, int userRegisId) throws ServletException, IOException {
	//AmsServices services = new AmsServices();
	AmsLicense al = new AmsLicense();
	logger.info(al.getLicenseInfo(userInfo.getUserName(), null));
	NodeID targetNodeID = null;
	String dobesCodeOfConductLicenseName = this.getServletContext().getInitParameter("DOBES_COC_LICENSE_NAME");
	logger.info("dobesCodeOfConductLicenseName: " + dobesCodeOfConductLicenseName);
	if (al.acceptLicenseInfo(userInfo.getUserName(), targetNodeID, dobesCodeOfConductLicenseName)) {
	    logger.info("*** END OF REGISTRATION for user: " + userInfo.getUserName());
	    logger.info("");
	    return true;
	} else {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Licence " + dobesCodeOfConductLicenseName);
	    errorRequest.setErrorMessage("Can't accept license");
	    errorRequest.setErrorValue(Integer.toString(userRegisId));
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("CANNOT_ACCEPT_LICENSE");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorRecoverable(false);
	    logger.error("Can't accept license: " + dobesCodeOfConductLicenseName + " for " + userInfo.getUserName() + " to AMS2 DB.");
	}
	return false;
    }

    private boolean addUser(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, RegistrationUser userInfo) throws ServletException, IOException {
	UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE);
	if (ug.isExistingUserName(userInfo.getUserName())) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Registration Email Check");
	    errorRequest.setErrorMessage("User: " + userInfo.getUserName() + " exists already");
	    errorRequest.setErrorValue("Please registrate again.");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("USER_EXISTS_IN_AMS_DATABASE");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorsHtmlTable();
	    String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
	    request.setAttribute("htmlErrorTable", htmlErrorTable);
	    logger.error("Email check; User: " + userInfo.getUserName() + " exists already in AMS database.");
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorOther.jsp");
	    view.forward(request, response);
	    return false;
	}
	return ug.addNewUser(userInfo);
    }

    private boolean sendRegistrationEmail(HttpServletRequest request, HttpServletResponse response, RrsRegistration rrsRegistration) throws ServletException, IOException {
	ErrorsRequest errorsRequestEmail = new ErrorsRequest();

	String corpmanEmail = (String) this.getServletContext().getAttribute("emailAddressCorpman");
	String emailHost = (String) this.getServletContext().getAttribute("emailHost");
	String userEmail = rrsRegistration.getUser().getEmail();

	EmailBean emailer = new EmailBean();
	rrsRegistration.setEmailAccountDetailsContent();

	emailer.setSubject("Max Planck Institute registration");
	emailer.setContent(rrsRegistration.getEmailAccountDetailsContent());
	emailer.setTo(corpmanEmail);
	emailer.setCc(userEmail);
	emailer.setFrom(corpmanEmail);
	emailer.setSmtpHost(emailHost);

	try {
	    emailer.sendMessage();
	    return true;
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
	    logger.error("Unable to send account details for user " + rrsRegistration.getUser().getUserName() + " with email address " + userEmail);
	    logger.error("RrsServlet javax.mail.SendFailedException: can't send email", e);
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
	    view.forward(request, response);
	    return false;
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
	    logger.error("Unable to send account details for user " + rrsRegistration.getUser().getUserName() + " with email address " + userEmail);
	    logger.error("java.lang.Exception: can't send email", e);
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
	    view.forward(request, response);
	    return false;
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
