/*
 * RrsIndex2.java
 *
 * Created on March 7, 2007, 12:17 PM
 */
package nl.mpi.rrs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.mpi.corpusstructure.CorpusStructureDB;
import nl.mpi.rrs.RrsConstants;

import nl.mpi.rrs.model.errors.ErrorsRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Show Resource Request Form
 * @author kees
 * @version
 */
public class RrsIndex2 extends HttpServlet {

    private final static Log logger = LogFactory.getLog(RrsIndex2.class);

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("RrsIndex2: *************** start *****************");
        logger.debug("getContextPath :" + request.getContextPath());
        logger.debug("getPathInfo :" + request.getPathInfo());
        logger.debug("getPathTranslated :" + request.getPathTranslated());
        logger.debug("getRequestURI :" + request.getRequestURI());
        logger.debug("getRequestURL :" + request.getRequestURL());
        logger.debug("getServletPath :" + request.getServletPath());

        ErrorsRequest errorsRequest = new ErrorsRequest();

        CorpusStructureDB corpusDbConnection = (CorpusStructureDB) this.getServletContext().getAttribute(RrsConstants.CORPUS_DB_CONNECTION_ATTRIBUTE);
        String htmlSelectedNodesTable = (String) request.getSession().getAttribute("htmlSelectedNodesTable");
        if (htmlSelectedNodesTable == null) {
            htmlSelectedNodesTable = "";
        }

        if (RrsIndex.createNodesTable(request, response, corpusDbConnection, errorsRequest, htmlSelectedNodesTable)) {
            RrsIndex.createCalendarDropdowns(request);
        }

        RrsIndex.dispatchServlet(request, response, errorsRequest, getServletContext());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
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
        return "Resource Request System Start Servlet phase 2";
    }
    // </editor-fold>
}
