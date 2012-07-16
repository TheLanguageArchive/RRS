/*
 * RRSServlet.java
 *
 * Created on January 31, 2007, 12:33 PM
 */
package nl.mpi.rrs.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mpi.corpusstructure.ArchiveObjectsDB;
import nl.mpi.corpusstructure.CorpusStructureDB;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.rrs.RrsConstants;
import nl.mpi.rrs.authentication.AuthenticationProvider;
import nl.mpi.rrs.model.RrsRequest;
import nl.mpi.rrs.model.corpusdb.ImdiNode;
import nl.mpi.rrs.model.corpusdb.ImdiNodes;
import nl.mpi.rrs.model.date.RrsDate;
import nl.mpi.rrs.model.email.EmailBean;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.errors.RrsGeneralException;
import nl.mpi.rrs.model.user.RequestUser;
import nl.mpi.rrs.model.user.User;
import nl.mpi.rrs.model.user.UserGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Verify Resource Request Form
 * Send email to user and corpman
 *
 * @author kees
 * @version
 */
public class RrsServlet extends HttpServlet {

    private static Log logger = LogFactory.getLog(RrsServlet.class);
    private AuthenticationProvider authenticationProvider;

    @Override
    public void init() throws ServletException {
	super.init();
	// Get authentication utility from servlet context. It is put there through
	// spring configuration in spring-rrs-auth(-test).xml
	authenticationProvider = (AuthenticationProvider) getServletContext().getAttribute(RrsConstants.AUTHENTICATION_PROVIDER_ATTRIBUTE);
    }

    /** Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, UnknownNodeException, RrsGeneralException {

	RrsRequest rrsRequest = new RrsRequest();
	ErrorsRequest errorsRequest = new ErrorsRequest();

	CorpusStructureDB corpusDbConnection = initCorpusStructureDbConnection(errorsRequest);
	ArchiveObjectsDB archiveObjectsDbConnection = initArchiveObjectsDbConnection(errorsRequest);
	UserGenerator ug = initUserGenerator(errorsRequest);

	if (ug != null && corpusDbConnection != null) {
	    initRequest(request, rrsRequest, ug, corpusDbConnection, archiveObjectsDbConnection, errorsRequest);
	}
	dispatchServlet(request, response, errorsRequest, rrsRequest);
    }

    private void initRequest(HttpServletRequest request, RrsRequest rrsRequest, UserGenerator userGenerator, CorpusStructureDB corpusDbConnection, ArchiveObjectsDB archiveObjectsDbConnection, ErrorsRequest errorsRequest) {
	String openPathPrefix = (String) this.getServletContext().getAttribute(RrsConstants.OPEN_PATH_PREFIX_ATTRIBUTE);
	ImdiNode.setOpenPathPrefix(openPathPrefix);
	logger.debug("openPathPrefix: " + openPathPrefix);

	RequestUser userInfo = initRequestUser(request, rrsRequest, userGenerator, errorsRequest);
	if (userInfo != null) {
	    initRequestDates(request, rrsRequest, errorsRequest);
	    initRequestNodes(request, rrsRequest, userInfo, corpusDbConnection, archiveObjectsDbConnection, errorsRequest);

	    rrsRequest.setRemarksOther(request.getParameter("paramRequestRemarksOther"));
	    rrsRequest.setPublicationAim(request.getParameter("paramRequestPublicationAim"));
	    rrsRequest.setResearchProject(request.getParameter("paramRequestResearchProject"));

	    rrsRequest.setIdentityProviderId(authenticationProvider.getIdentityProviderId(request));

	    request.setAttribute("rrsRequest", rrsRequest);
	}
    }

    /**
     *
     * @param errorsRequest Collection to add to in case of error
     * @return null if db connection is not present
     */
    private CorpusStructureDB initCorpusStructureDbConnection(ErrorsRequest errorsRequest) {
	CorpusStructureDB corpusDbConnection = (CorpusStructureDB) this.getServletContext().getAttribute(RrsConstants.CORPUS_DB_CONNECTION_ATTRIBUTE);
	if (corpusDbConnection == null) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Corpus Database");
	    errorRequest.setErrorMessage("Server is down");
	    errorRequest.setErrorValue("");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("CORPUS_DATABASE_DOWN");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorRecoverable(false);
	}
	return corpusDbConnection;
    }

    private ArchiveObjectsDB initArchiveObjectsDbConnection(ErrorsRequest errorsRequest) {
	ArchiveObjectsDB archiveObjectsDbConnection = (ArchiveObjectsDB) this.getServletContext().getAttribute(RrsConstants.ARCHIVE_OBJECTS_DB_CONNECTION_ATTRIBUTE);
	if (archiveObjectsDbConnection == null) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Archive objects database");
	    errorRequest.setErrorMessage("Server is down");
	    errorRequest.setErrorValue("");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("CORPUS_DATABASE_DOWN");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorRecoverable(false);
	}
	return archiveObjectsDbConnection;
    }

    /**
     *
     * @param errorsRequest Collection to add to in case of error
     * @return null if user generator is not present
     */
    private UserGenerator initUserGenerator(ErrorsRequest errorsRequest) {
	UserGenerator ug = (UserGenerator) this.getServletContext().getAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE);
	if (ug == null) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("AMS Database");
	    errorRequest.setErrorMessage("Server is down");
	    errorRequest.setErrorValue("");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("AMS_DATABASE_DOWN");
	    errorRequest.setErrorRecoverable(false);
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorRecoverable(false);
	}
	return ug;
    }

    private RequestUser initRequestUser(HttpServletRequest request, RrsRequest rrsRequest, UserGenerator ug, ErrorsRequest errorsRequest) {
	RequestUser userInfo = new RequestUser();
	if (authenticationProvider.isUserLoggedIn(request) && ug.isExistingUserName(authenticationProvider.getLoggedInUser(request))) {
	    rrsRequest.setUserStatus("Existing user");
	    String userName = authenticationProvider.getLoggedInUser(request);
	    logger.debug("Username: " + userName);
	    logger.debug("using UserGenerator " + ug.getInfo());
	    /*
	     * user.setPassword(request.getParameter("paramUserOldPassword"));
	     * String passWord = user.getPassword();
	     * if (ug.isValidPasswordForUsername(userName, passWord)) {
	     */
	    User userDB = ug.getUserInfoByUserName(userName);
	    if (userDB != null) {
		userInfo.setFirstName(userDB.getFirstName());
		userInfo.setLastName(userDB.getLastName());
		userInfo.setEmail(userDB.getEmail());
		userInfo.setOrganization(userDB.getOrganization());
		userInfo.setUserName(userDB.getUserName());
		logger.debug("** Got AMS2 connection for user: " + userInfo.getFullName());
		logger.debug("** name: " + userInfo.getLastName());
		logger.debug("** email address: " + userInfo.getEmail());
	    } else {
		ErrorRequest errorRequest = new ErrorRequest();
		errorRequest.setErrorFormFieldLabel("Form field: Username");
		errorRequest.setErrorMessage("No info in database for username");
		errorRequest.setErrorValue(userName);
		errorRequest.setErrorException(null);
		errorRequest.setErrorType("INVALID_USER_ID");
		errorRequest.setErrorRecoverable(true);
		errorsRequest.addError(errorRequest);
		logger.debug("Invalid AMS username: " + userName);
	    }
	    request.setAttribute("user", userInfo);
	    rrsRequest.setUser(userInfo);
	    return userInfo;
	} else {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Form field: Username");
	    errorRequest.setErrorMessage("No user is authenticated");
	    errorRequest.setErrorValue(rrsRequest.getUserStatus());
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("USER_NOT_AUTHENTICATED");
	    errorRequest.setErrorRecoverable(true);
	    errorsRequest.addError(errorRequest);
	    logger.debug("No authenticated user");
	    return null;
	}
    }

    private void initRequestDates(HttpServletRequest request, RrsRequest rrsRequest, ErrorsRequest errorsRequest) {
	RrsDate fromDate = new RrsDate();
	fromDate.setDay(request.getParameter("paramRequestFromDateDay"));
	fromDate.setMonth(request.getParameter("paramRequestFromDateMonth"));
	fromDate.setYear(request.getParameter("paramRequestFromDateYear"));
	fromDate.setValue();
	if (!fromDate.isAValidDate()) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Form field: Period From date");
	    errorRequest.setErrorMessage("Invalid date");
	    errorRequest.setErrorValue(fromDate.getValue());
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("INVALID_DATE");
	    errorRequest.setErrorRecoverable(true);
	    errorsRequest.addError(errorRequest);
	    logger.debug("Invalid date: " + fromDate.getValue());
	}
	RrsDate toDate = new RrsDate();
	toDate.setDay(request.getParameter("paramRequestToDateDay"));
	toDate.setMonth(request.getParameter("paramRequestToDateMonth"));
	toDate.setYear(request.getParameter("paramRequestToDateYear"));
	toDate.setValue();
	if (!toDate.isAValidDate()) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Form field: Period To date");
	    errorRequest.setErrorMessage("Invalid date");
	    errorRequest.setErrorValue(toDate.getValue());
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("INVALID_DATE");
	    errorRequest.setErrorRecoverable(true);
	    errorsRequest.addError(errorRequest);
	    logger.debug("Invalid date: " + toDate.getValue());
	}
	if (fromDate.isLaterThan(toDate.toCalendar())) {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Form field: Period From/To");
	    errorRequest.setErrorMessage("Invalid period");
	    errorRequest.setErrorValue(fromDate.getValue() + " - " + toDate.getValue());
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("INVALID_DATE_PERIOD");
	    errorRequest.setErrorRecoverable(true);
	    errorsRequest.addError(errorRequest);
	    logger.debug("Invalid period: " + fromDate.getValue() + " - " + toDate.getValue());
	}
	rrsRequest.setFromDate(fromDate);
	rrsRequest.setToDate(toDate);
    }

    private void initRequestNodes(HttpServletRequest request, RrsRequest rrsRequest, RequestUser userInfo, CorpusStructureDB corpusDbConnection, ArchiveObjectsDB archiveObjectsDbConnection, ErrorsRequest errorsRequest) {
	String[] values = request.getParameterValues("nodeid");
	ImdiNodes imdiNodes = new ImdiNodes();
	if (values != null) {
	    rrsRequest.setNodesEnteredInForm(false);
	    if (values.length > 0) {
		for (int i = 0; i < values.length; i++) {
		    if (values[i] != null && !(values[i].equalsIgnoreCase(""))) {
			logger.debug("Param values: " + values[i]);
			ImdiNode imdiNode = new ImdiNode();
			imdiNode.setImdiNodeIdWithPrefix(values[i]);
			imdiNodes.addImdiNode(imdiNode);
		    }
		}
	    }
	}

	for (int i = 0; i < imdiNodes.getSize(); i++) {
	    ImdiNode imdiNode = imdiNodes.getImdiNode(i);
	    String nodeIdWithPrefix = imdiNode.getImdiNodeIdWithPrefix();
	    if (nodeIdWithPrefix != null) {
		try {
		    imdiNode.setImdiNodeName(corpusDbConnection.getNode(nodeIdWithPrefix).getName());
		    imdiNode.setImdiNodeFormat(corpusDbConnection.getNode(nodeIdWithPrefix).getFormat());
		    imdiNode.setImdiNodeUrl(corpusDbConnection.getNamePath(nodeIdWithPrefix));
		    imdiNode.setImdiNodeUri(archiveObjectsDbConnection.getObjectURI(nodeIdWithPrefix).toString());
		    imdiNodes.setImdiNode(i, imdiNode);
		} catch (UnknownNodeException ex) {
		    logger.warn("Exception while processing access request nodes", ex);
		    ErrorRequest errorRequest = new ErrorRequest();
		    if (rrsRequest.isNodesEnteredInForm()) {
			errorRequest.setErrorFormFieldLabel("Form field: Node Id");
		    } else {
			errorRequest.setErrorFormFieldLabel("Browser selected: Node Id");
		    }
		    errorRequest.setErrorValue(nodeIdWithPrefix);
		    errorRequest.setErrorMessage("Invalid NodeId");
		    errorRequest.setErrorException("mpi.corpusstructure.UnknownNodeException");
		    errorRequest.setErrorType("INVALID_NODE_ID");
		    errorRequest.setErrorRecoverable(true);
		    errorsRequest.addError(errorRequest);
		}
	    }
	}
	if (imdiNodes.getSize() > 0) {
	    rrsRequest.setImdiNodes(imdiNodes);
	} else {
	    ErrorRequest errorRequest = new ErrorRequest();
	    errorRequest.setErrorFormFieldLabel("Form field: Node Id");
	    errorRequest.setErrorValue("no values");
	    errorRequest.setErrorMessage("No NodeId's filled out");
	    errorRequest.setErrorException(null);
	    errorRequest.setErrorType("EMPTY_NODE_ID");
	    errorRequest.setErrorRecoverable(true);
	    errorsRequest.addError(errorRequest);
	}
    }

    private void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, RrsRequest rrsRequest)
	    throws ServletException, IOException {

	logger.debug("errorsRequest.getSize(): " + errorsRequest.getSize());

	if (errorsRequest.getSize() > 0) {
	    RequestDispatcher view;
	    if (errorsRequest.isErrorRecoverable()) {
		errorsRequest.setErrorsHtmlTable();
		String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
		request.setAttribute("htmlErrorTable", htmlErrorTable);
		logger.debug("errorsRequest.isErrorRecoverable: call view/error/error.jsp");
		view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
	    } else {
		logger.debug("NOT errorsRequest.isErrorRecoverable: call view/error/errorUnknown.jsp");
		view = request.getRequestDispatcher("/WEB-INF/view/error/errorUnknown.jsp");
	    }
	    view.forward(request, response);
	    return;
	} else {
	    if (sendMail(rrsRequest, request, errorsRequest, response)) {
		logger.debug("RrsServlet: No Exception");
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/result.jsp");
		view.forward(request, response);
	    }
	}
    }

    private boolean sendMail(RrsRequest rrsRequest, HttpServletRequest request, ErrorsRequest errorsRequest, HttpServletResponse response) throws ServletException, IOException {
	EmailBean emailer = new EmailBean();
	rrsRequest.setEmailContent();
	emailer.setSubject("Resource Request System");
	emailer.setContent(rrsRequest.getEmailContent());
	String corpmanEmail = (String) this.getServletContext().getAttribute(RrsConstants.EMAIL_ADDRESS_CORPMAN_ATTRIBUTE);
	String emailHost = (String) this.getServletContext().getAttribute(RrsConstants.SMTP_HOST_ATTRIBUTE);
	String userEmail = rrsRequest.getUser().getEmail();
	request.setAttribute("emailAddressCorpman", corpmanEmail);
	request.setAttribute("emailHost", emailHost);
	emailer.setTo(corpmanEmail);
	emailer.setCc(userEmail);
	emailer.setFrom(corpmanEmail);
	emailer.setSmtpHost(emailHost);
	logger.debug("From: " + corpmanEmail);
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
	    errorsRequest.addError(errorRequest);
	    errorsRequest.setErrorsHtmlTable();
	    String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
	    request.setAttribute("htmlErrorTable", htmlErrorTable);
	    logger.error("RrsServlet java.lang.Exception: can't send email", e);
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
	    view.forward(request, response);
	    return false;
	}
	return true;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    /** Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public java.lang.String getServletInfo() {
	return "Resource Request System Controller Servlet";
    }
    // </editor-fold>
}
