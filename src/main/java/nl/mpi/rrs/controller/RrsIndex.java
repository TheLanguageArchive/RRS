/*
 * RrsIndex.java
 *
 * Created on March 7, 2007, 12:17 PM
 */
package nl.mpi.rrs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mpi.corpusstructure.ArchiveObjectsDB;
import nl.mpi.corpusstructure.CorpusStructureDB;
import nl.mpi.corpusstructure.UnknownNodeException;
import nl.mpi.rrs.RrsConstants;
import nl.mpi.rrs.authentication.AuthenticationProvider;
import nl.mpi.rrs.model.corpusdb.ImdiNode;
import nl.mpi.rrs.model.date.PulldownGenerator;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.user.UserGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Show Resource Request Form
 *
 * @author kees
 * @version
 */
public class RrsIndex extends HttpServlet {

    public static final String SHIBSTATE_COOKIE_PREFIX = "_shibstate";
    private static Log logger = LogFactory.getLog(RrsIndex.class);

    /** Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	logger.debug("RrsIndex: *************** start *****************");
	logger.debug("getContextPath :" + request.getContextPath());
	logger.debug("getPathInfo :" + request.getPathInfo());
	logger.debug("getPathTranslated :" + request.getPathTranslated());
	logger.debug("getRequestURI :" + request.getRequestURI());
	logger.debug("getRequestURL :" + request.getRequestURL());
	logger.debug("getServletPath :" + request.getServletPath());

	String urlRrsRegistration = request.getContextPath() + "/RrsRegistration";
	request.setAttribute("urlRrsRegistration", urlRrsRegistration);

	ErrorsRequest errorsRequest = new ErrorsRequest();
	CorpusStructureDB corpusDbConnection = (CorpusStructureDB) this.getServletContext().getAttribute(RrsConstants.CORPUS_DB_CONNECTION_ATTRIBUTE);
	ArchiveObjectsDB archiveObjectsConnection = (ArchiveObjectsDB) this.getServletContext().getAttribute(RrsConstants.ARCHIVE_OBJECTS_DB_CONNECTION_ATTRIBUTE);
	if (createNodesTable(request, response, corpusDbConnection, archiveObjectsConnection, errorsRequest, "")) {
	    createCalendarDropdowns(request);
	}
	dispatchServlet(request, response, errorsRequest, getServletContext());
    }

    static boolean createNodesTable(HttpServletRequest request, HttpServletResponse response, CorpusStructureDB corpusDbConnection, ArchiveObjectsDB archiveObjectsConnection, ErrorsRequest errorsRequest, String htmlSelectedNodesTable) throws IOException, ServletException {
	logger.debug("RrsIndex2 Context: htmlSelectedNodesTable:" + htmlSelectedNodesTable);
	if (htmlSelectedNodesTable == null) {
	    htmlSelectedNodesTable = "";
	}
	if (htmlSelectedNodesTable.length() == 0) {
	    List<String> values = getNodeIds(request, archiveObjectsConnection);
	    if (values.size() > 0) {
		if (corpusDbConnection == null) {
		    logger.warn("RrsIndex: *************** corpusDbConnection == null ");
		    ErrorRequest errorRequest = new ErrorRequest();
		    errorRequest.setErrorFormFieldLabel("Corpus Database");
		    errorRequest.setErrorMessage("Server is down");
		    errorRequest.setErrorValue("");
		    errorRequest.setErrorException(null);
		    errorRequest.setErrorType("CORPUS_DATABASE_DOWN");
		    errorsRequest.addError(errorRequest);
		    errorsRequest.setErrorRecoverable(false);
		    return false;
		}

		//for (int i = 0; i < values.length; i++) {
		int i = 0;
		for (String nodeId : values) {
		    if (nodeId != null && !(nodeId.equalsIgnoreCase(""))) {
			logger.debug("RrsIndex: *************** Param nodeId: " + nodeId);
			ImdiNode imdiNode = new ImdiNode();
			imdiNode.setImdiNodeIdWithPrefix(nodeId);
			String nodeIdWithPrefix = imdiNode.getImdiNodeIdWithPrefix();
			if (nodeIdWithPrefix != null) {
			    try {
				imdiNode.setImdiNodeName(corpusDbConnection.getNode(nodeIdWithPrefix).getName());
				String nodeNameHtml = "paramNodeName_" + i++;
				String nodeNameValue = imdiNode.getImdiNodeName();
				String nodeIdHtml = "nodeid";
				htmlSelectedNodesTable += "<tr>";
				htmlSelectedNodesTable += "    <td> <input type=\"text\" name=\"" + nodeIdHtml + "\"  value = \"" + nodeId + "\" readonly=\"readonly\" size=\"30\" /> ";
				htmlSelectedNodesTable += "    </td> ";
				htmlSelectedNodesTable += "    <td> <input type=\"text\" name=\"" + nodeNameHtml + "\"  value = \"" + nodeNameValue + "\" readonly=\"readonly\" size=\"50\" /> ";
				htmlSelectedNodesTable += "    </td> ";
				htmlSelectedNodesTable += " </tr> ";
			    } catch (UnknownNodeException ex) {
				ErrorRequest errorRequest = new ErrorRequest();
				errorRequest.setErrorFormFieldLabel("Browser selected: Node Id");
				errorRequest.setErrorValue(nodeId);
				errorRequest.setErrorMessage("Invalid NodeId");
				errorRequest.setErrorException("mpi.corpusstructure.UnknownNodeException");
				errorRequest.setErrorType("INVALID_NODE_ID");
				errorsRequest.addError(errorRequest);
				errorsRequest.setErrorFromBrowser(true);
			    }
			} else {
			    ErrorRequest errorRequest = new ErrorRequest();
			    errorRequest.setErrorFormFieldLabel("Browser selected: Node Id");
			    errorRequest.setErrorValue(nodeId);
			    errorRequest.setErrorMessage("Invalid NodeId");
			    errorRequest.setErrorException("mpi.corpusstructure.UnknownNodeException");
			    errorRequest.setErrorType("INVALID_NODE_ID");
			    errorsRequest.addError(errorRequest);
			    errorsRequest.setErrorFromBrowser(true);
			}
		    }
		}
	    }
	}
	logger.debug("RrsIndex: htmlSelectedNodesTable:" + htmlSelectedNodesTable);
	request.getSession().setAttribute("htmlSelectedNodesTable", htmlSelectedNodesTable);
	return true;
    }

    static void createCalendarDropdowns(HttpServletRequest request) {
	Calendar cal = Calendar.getInstance();
	PulldownGenerator datePullDownToday = new PulldownGenerator(cal);
	String pulldownDayOfMonthToday = datePullDownToday.pulldownDayOfMonth("paramRequestFromDateDay");
	String pulldownMonthOfYearToday = datePullDownToday.pulldownMonthOfYear("paramRequestFromDateMonth");
	String pulldownYearToday = datePullDownToday.pulldownYear("paramRequestFromDateYear");
	cal.add(Calendar.YEAR, 1);
	int year = cal.get(Calendar.YEAR);
	PulldownGenerator datePullDownEnd = new PulldownGenerator(cal);
	String pulldownDayOfMonthEnd = datePullDownEnd.pulldownDayOfMonth("paramRequestToDateDay");
	String pulldownMonthOfYearEnd = datePullDownEnd.pulldownMonthOfYear("paramRequestToDateMonth");
	String pulldownYearEnd = datePullDownEnd.pulldownYear("paramRequestToDateYear", year - 1, year + 9);
	request.setAttribute("pulldownDayOfMonthToday", pulldownDayOfMonthToday);
	request.setAttribute("pulldownMonthOfYearToday", pulldownMonthOfYearToday);
	request.setAttribute("pulldownYearToday", pulldownYearToday);
	request.setAttribute("pulldownDayOfMonthEnd", pulldownDayOfMonthEnd);
	request.setAttribute("pulldownMonthOfYearEnd", pulldownMonthOfYearEnd);
	request.setAttribute("pulldownYearEnd", pulldownYearEnd);
    }

    static void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, ServletContext servletContext)
	    throws ServletException, IOException {
	if (errorsRequest.getSize() > 0) {
	    if (errorsRequest.isErrorFromBrowser()) {
		errorsRequest.setErrorsHtmlTable();
		String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
		request.setAttribute("htmlErrorTable", htmlErrorTable);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorBrowser.jsp");
		view.forward(request, response);
		return;

	    } else {
		errorsRequest.setErrorsHtmlTable();
		String htmlErrorTable = errorsRequest.getErrorsHtmlTable();
		request.setAttribute("htmlErrorTable", htmlErrorTable);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorUnknown.jsp");
		view.forward(request, response);
		return;
	    }
	} else {
	    UserGenerator ug = (UserGenerator) servletContext.getAttribute(RrsConstants.AMS2_DB_CONNECTION_ATTRIBUTE);
	    AuthenticationProvider authProvider = (AuthenticationProvider) servletContext.getAttribute(RrsConstants.AUTHENTICATION_PROVIDER_ATTRIBUTE);

	    assert ug != null;
	    assert authProvider != null;

	    boolean loggedIn = authProvider.isUserLoggedIn(request);
	    if (loggedIn) {
		request.setAttribute("uid", authProvider.getLoggedInUser(request));
	    }

	    if (loggedIn && ug.isExistingUserName(authProvider.getLoggedInUser(request))) {
		logger.debug("RrsIndex: call index_2.jsp");

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/index_2.jsp");
		view.forward(request, response);
	    } else {
		logger.debug("RrsIndex: call index.jsp");

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/index.jsp");
		view.forward(request, response);
	    }
	}
    }

    private static List<String> getNodeIds(HttpServletRequest request, ArchiveObjectsDB archiveObjectsConnection) {
	List<String> values = new ArrayList<String>();

	final String[] nodeIdValues = request.getParameterValues("nodeid");
	if (nodeIdValues != null) {
	    values.addAll(Arrays.asList(nodeIdValues));
	}
	return values;
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
    public String getServletInfo() {
	return "Resource Request System Start Servlet";
    }
    // </editor-fold>
}
