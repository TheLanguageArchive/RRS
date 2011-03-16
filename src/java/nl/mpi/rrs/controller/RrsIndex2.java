/*
 * RrsIndex2.java
 *
 * Created on March 7, 2007, 12:17 PM
 */
package nl.mpi.rrs.controller;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mpi.corpusstructure.CorpusStructureDBImpl;
import mpi.corpusstructure.UnknownNodeException;
import nl.mpi.rrs.model.corpusdb.ImdiNode;
import nl.mpi.rrs.model.date.PulldownGenerator;
import nl.mpi.rrs.model.errors.ErrorRequest;
import nl.mpi.rrs.model.errors.ErrorsRequest;
import nl.mpi.rrs.model.utilities.RrsUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Show Resource Request Form
 * @author kees
 * @version
 */
public class RrsIndex2 extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Logger logger = Logger.getLogger("RrsIndex2");
        logger.setLevel(Level.INFO);

        logger.info("RrsIndex2: *************** start *****************");
        logger.info("getContextPath :" + request.getContextPath());
        logger.info("getPathInfo :" + request.getPathInfo());
        logger.info("getPathTranslated :" + request.getPathTranslated());
        logger.info("getRequestURI :" + request.getRequestURI());
        logger.info("getRequestURL :" + request.getRequestURL());
        logger.info("getServletPath :" + request.getServletPath());

        String htmlSelectedNodesTable;
        //String htmlSelectedNodesTable = (String) request.getAttribute("htmlSelectedNodesTable");
        //logger.info("RrsIndex2: htmlSelectedNodesTable:" + htmlSelectedNodesTable);
        
        //htmlSelectedNodesTable = (String) this.getServletContext().getAttribute("htmlSelectedNodesTable");
        HttpSession session = request.getSession();
        htmlSelectedNodesTable = (String) session.getAttribute("htmlSelectedNodesTable");
        logger.info("RrsIndex2 Context: htmlSelectedNodesTable:" + htmlSelectedNodesTable);
        
        String checkEmailLinkBase = "";
        String urlFields[] = request.getRequestURL().toString().split("/");

        for (int i = 0; i < urlFields.length - 1; i++) {
            checkEmailLinkBase += urlFields[i];
            checkEmailLinkBase += "/";
        }

        checkEmailLinkBase += "RrsRegistration";

        logger.info("Url RrsRegistration: " + checkEmailLinkBase);
        request.setAttribute("RrsRegistration", checkEmailLinkBase);

        String urlRrsRegistration = request.getContextPath() + "/RrsRegistration";
        request.setAttribute("urlRrsRegistration", urlRrsRegistration);

        ErrorsRequest errorsRequest = new ErrorsRequest();
        

        String[] values = request.getParameterValues("nodeid");
        if (values != null) {
            if (values.length > 0) {
                

                CorpusStructureDBImpl corpusDbConnection = (CorpusStructureDBImpl) this.getServletContext().getAttribute("corpusDbConnection");

                if (corpusDbConnection == null) {

                    logger.info("RrsIndex2: *************** corpusDbConnection == null ");

                    ErrorRequest errorRequest = new ErrorRequest();

                    errorRequest.setErrorFormFieldLabel("Corpus Database");
                    errorRequest.setErrorMessage("Server is down");
                    errorRequest.setErrorValue("");
                    errorRequest.setErrorException(null);
                    errorRequest.setErrorType("CORPUS_DATABASE_DOWN");

                    errorsRequest.addError(errorRequest);
                    errorsRequest.setErrorRecoverable(false);

                    dispatchServlet(request, response, errorsRequest);
                    return;
                }

                for (int i = 0; i < values.length; i++) {
                    
                    if (values[i] != null && !(values[i].equalsIgnoreCase(""))) {

                        logger.info("RrsIndex2: *************** Param nodeId: " + values[i]);

                        ImdiNode imdiNode = new ImdiNode();
                        imdiNode.setImdiNodeIdWithPrefix(values[i]);
                        String nodeIdWithPrefix = imdiNode.getImdiNodeIdWithPrefix();
                        if (nodeIdWithPrefix != null) {
                            try {
                                imdiNode.setImdiNodeName(corpusDbConnection.getNode(nodeIdWithPrefix).getName());
                                String nodeNameHtml = "paramNodeName_" + i;
                                String nodeNameValue = imdiNode.getImdiNodeName();

                                String nodeIdHtml = "nodeid";
                                //paramNodeId_" + i;
                                String nodeIdValue = values[i];

                                htmlSelectedNodesTable += "<tr>";
                                htmlSelectedNodesTable += "    <td> <input type=\"text\" name=\"" + nodeIdHtml + "\"  value = \"" + nodeIdValue + "\" readonly=\"readonly\" size=\"30\" /> ";
                                htmlSelectedNodesTable += "    </td> ";
                                htmlSelectedNodesTable += "    <td> <input type=\"text\" name=\"" + nodeNameHtml + "\"  value = \"" + nodeNameValue + "\" readonly=\"readonly\" size=\"50\" /> ";
                                htmlSelectedNodesTable += "    </td> ";
                                htmlSelectedNodesTable += " </tr> ";



                            } catch (UnknownNodeException ex) {
                                ErrorRequest errorRequest = new ErrorRequest();

                                errorRequest.setErrorFormFieldLabel("Browser selected: Node Id");
                                errorRequest.setErrorValue(values[i]);
                                errorRequest.setErrorMessage("Invalid NodeId");
                                errorRequest.setErrorException("mpi.corpusstructure.UnknownNodeException");
                                errorRequest.setErrorType("INVALID_NODE_ID");

                                errorsRequest.addError(errorRequest);
                                errorsRequest.setErrorFromBrowser(true);
                            }


                        } else {
                            ErrorRequest errorRequest = new ErrorRequest();

                            errorRequest.setErrorFormFieldLabel("Browser selected: Node Id");
                            errorRequest.setErrorValue(values[i]);
                            errorRequest.setErrorMessage("Invalid NodeId");
                            errorRequest.setErrorException("mpi.corpusstructure.UnknownNodeException");
                            errorRequest.setErrorType("INVALID_NODE_ID");

                            errorsRequest.addError(errorRequest);
                            errorsRequest.setErrorFromBrowser(true);
                        }

                    }
                }
            }
        } else {
            logger.info("RrsIndex2: no nodeIds");
        }

        Calendar cal = Calendar.getInstance();

        PulldownGenerator datePullDownToday = new PulldownGenerator(cal);

        String pulldownDayOfMonthToday = datePullDownToday.pulldownDayOfMonth("paramRequestFromDateDay");
        String pulldownMonthOfYearToday = datePullDownToday.pulldownMonthOfYear("paramRequestFromDateMonth");
        String pulldownYearToday = datePullDownToday.pulldownYear("paramRequestFromDateYear");

        cal.set(cal.YEAR, cal.get(cal.YEAR) + 1);

        int year = cal.get(cal.YEAR);
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

        //request.setAttribute("htmlSelectedNodesTable", request.getAttribute(htmlSelectedNodesTable));
        //request.setAttribute("htmlSelectedNodesTable", htmlSelectedNodesTable);
        //this.getServletContext().setAttribute("htmlSelectedNodesTable", htmlSelectedNodesTable);
        //HttpSession session = request.getSession();
        session.setAttribute("htmlSelectedNodesTable", htmlSelectedNodesTable);

        dispatchServlet(request, response, errorsRequest);
        return;
    }

    public void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest)
            throws ServletException, IOException {

        Logger logger = Logger.getLogger("RrsIndex2");
        logger.setLevel(Level.INFO);

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
            //String fromFirstPage = (String) request.getParameter("fromFirstPage");
            //logger.info("fromFirstPage: " + fromFirstPage);

            String uidFromShib = request.getRemoteUser();
            //if (uidFromShib.equalsIgnoreCase("anonymous") && RrsUtil.isEmpty(fromFirstPage)) {
            if (uidFromShib.equalsIgnoreCase("anonymous")) {

                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/index.jsp");
                view.forward(request, response);
            } else {

                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/index_2.jsp");
                view.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Resource Request System Start Servlet";
    }
    // </editor-fold>
}
