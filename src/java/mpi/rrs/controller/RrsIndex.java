/*
 * RrsIndex.java
 *
 * Created on March 7, 2007, 12:17 PM
 */

package mpi.rrs.controller;

import java.io.*;
import java.net.*;
import java.util.Calendar;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

import mpi.rrs.model.corpusdb.ImdiNode;
import mpi.rrs.model.errors.*;
import mpi.rrs.model.date.PulldownGenerator;

import mpi.corpusstructure.*;
/**
 *
 * @author kees
 * @version
 */
public class RrsIndex extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        Logger logger = Logger.getLogger("RrsIndex");
        
        ErrorsRequest errorsRequest = new ErrorsRequest();
        String htmlSelectedNodesTable = "";
        String htmlInputNodesTable = "";
        String htmlErrorTable = "";
        
        String[] values = request.getParameterValues("nodeid");
        if (values != null) {
            if (values.length > 0) {
                
                CorpusStructureDBImpl corpusDbConnection = (CorpusStructureDBImpl) this.getServletContext().getAttribute("corpusDbConnection");
                
                if (corpusDbConnection == null) {
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
                                htmlSelectedNodesTable += "    <td valign=\"top\"> <input type=\"text\" name=" + nodeIdHtml + "  value = " + nodeIdValue + " readonly size=\"30\"> ";
                                htmlSelectedNodesTable += "    </td> ";
                                htmlSelectedNodesTable += "    <td valign=\"top\"> <input type=\"text\" name=" + nodeNameHtml + "  value = " + nodeNameValue + " readonly size=\"50\"> ";
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
            
            int maxFormNodeIds = 10;
            String maxFormNodeIdsString = (String) getServletContext().getAttribute("maxFormNodeIds");
            
            if (maxFormNodeIdsString != null) {
                maxFormNodeIds = Integer.parseInt(maxFormNodeIdsString);
            }
            
            for (int i = 0; i < maxFormNodeIds; i++) {
                String name = "paramNodeId_" + i;
                
                htmlInputNodesTable += "<tr> ";
                htmlInputNodesTable += "    <td valign=\"top\"> <input type=\"text\" name=\"" + name + " size=\"30\"> ";
                htmlInputNodesTable += "    </td> ";
                htmlInputNodesTable += "</tr> ";
            }
            
        }
        
        Calendar cal = Calendar.getInstance();
        
        PulldownGenerator datePullDownToday = new PulldownGenerator(cal);
        
        String pulldownDayOfMonthToday = datePullDownToday.pulldownDayOfMonth("paramRequestFromDateDay");
        String pulldownMonthOfYearToday = datePullDownToday.pulldownMonthOfYear("paramRequestFromDateMonth");
        String pulldownYearToday = datePullDownToday.pulldownYear("paramRequestFromDateYear");
        
        cal.set(cal.YEAR,cal.get(cal.YEAR) + 1);
        
        int year = cal.get(cal.YEAR);
        PulldownGenerator datePullDownEnd = new PulldownGenerator(cal);
        
        String pulldownDayOfMonthEnd = datePullDownEnd.pulldownDayOfMonth("paramRequestToDateDay");
        String pulldownMonthOfYearEnd = datePullDownEnd.pulldownMonthOfYear("paramRequestToDateMonth");
        String pulldownYearEnd = datePullDownEnd.pulldownYear("paramRequestToDateYear", year-1, year+9);
        
        request.setAttribute("pulldownDayOfMonthToday",pulldownDayOfMonthToday);
        request.setAttribute("pulldownMonthOfYearToday",pulldownMonthOfYearToday);
        request.setAttribute("pulldownYearToday",pulldownYearToday);
        
        request.setAttribute("pulldownDayOfMonthEnd",pulldownDayOfMonthEnd);
        request.setAttribute("pulldownMonthOfYearEnd",pulldownMonthOfYearEnd);
        request.setAttribute("pulldownYearEnd",pulldownYearEnd);
        
        request.setAttribute("htmlInputNodesTable", htmlInputNodesTable);
        request.setAttribute("htmlSelectedNodesTable", htmlSelectedNodesTable);
        
        dispatchServlet(request, response, errorsRequest);
        return;
    }
    
    public void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest)
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
            
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/index.jsp");
            view.forward(request, response);
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
