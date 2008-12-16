/*
 * RRSServlet.java
 *
 * Created on January 31, 2007, 12:33 PM
 */
package mpi.rrs.controller;

import java.io.IOException;

//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mpi.corpusstructure.CorpusStructureDBImpl;
import mpi.corpusstructure.UnknownNodeException;
import mpi.rrs.model.RrsRequest;
import mpi.rrs.model.corpusdb.ImdiNode;
import mpi.rrs.model.corpusdb.ImdiNodes;
import mpi.rrs.model.date.RrsDate;
import mpi.rrs.model.email.EmailBean;
import mpi.rrs.model.errors.ErrorRequest;
import mpi.rrs.model.errors.ErrorsRequest;
import mpi.rrs.model.errors.RrsGeneralException;
import mpi.rrs.model.user.RequestUser;
import mpi.rrs.model.user.User;
import mpi.rrs.model.user.UserGenerator2;
import mpi.rrs.model.utilities.RrsUtil;
import mpi.rrs.model.ams.AmsServicesSingleton;
import mpi.rrs.model.ams.AmsLicense;
import nl.mpi.lat.fabric.Node;
import nl.mpi.lat.fabric.NodeID;


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

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnknownNodeException, RrsGeneralException {

        RrsRequest rrsRequest = new RrsRequest();
        ErrorsRequest errorsRequest = new ErrorsRequest();

        String openPathPrefix = this.getServletContext().getInitParameter("OPENPATH_PREFIX");
        ImdiNode.setOpenPathPrefix(openPathPrefix);
        logger.debug("openPathPrefix: " + openPathPrefix);

        //CorpusNodes.setDBCredentials();
        //CorpusStructureDBImpl corpusDbConnection = CorpusNodes.csdb;
        CorpusStructureDBImpl corpusDbConnection = (CorpusStructureDBImpl) this.getServletContext().getAttribute("corpusDbConnection");

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

            dispatchServlet(request, response, errorsRequest, rrsRequest);
            return;

        }

        String emailAddressCorpman = (String) this.getServletContext().getAttribute("emailAddressCorpman");
        request.setAttribute("emailAddressCorpman", emailAddressCorpman);

        String emailHost = (String) this.getServletContext().getAttribute("emailHost");
        request.setAttribute("emailHost", emailHost);

        ImdiNodes imdiNodes = new ImdiNodes();
        RequestUser userInfo = new RequestUser();
        RequestUser user = new RequestUser();

        user.setUserName(request.getParameter("paramUserOldUserName"));
        String userName = user.getUserName();

        // TODO: check and init user-generator properly
        if (RrsUtil.isNotEmpty(userName)) {
            rrsRequest.setUserStatus("Existing user");

            // ams2 : using defaults : user-data provider and authentication service 
            UserGenerator2 ug = (UserGenerator2) this.getServletContext().getAttribute("ams2DbConnection");
            
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
                
                dispatchServlet(request, response, errorsRequest, rrsRequest);
                return;
            } else {

                logger.info("using UserGenerator " + ug.getInfo());

                user.setPassword(request.getParameter("paramUserOldPassword"));
                String passWord = user.getPassword();

                if (ug.isValidPasswordForUsername(userName, passWord)) {

                    User userDB = ug.getUserInfoByUserName(userName);
                    if (userDB != null) {
                        userInfo.setFirstName(userDB.getFirstName());
                        userInfo.setLastName(userDB.getLastName());
                        userInfo.setEmail(userDB.getEmail());
                        userInfo.setOrganization(userDB.getOrganization());
                        userInfo.setUserName(userDB.getUserName());
                        logger.info("** Got AMS2 connection for user: " + userInfo.getFullName());
                        logger.debug("** name: " + userInfo.getLastName());
                        logger.info("** email address: " + userInfo.getEmail());

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


                } else {
                    ErrorRequest errorRequest = new ErrorRequest();

                    errorRequest.setErrorFormFieldLabel("Form field: Username/Password");
                    errorRequest.setErrorMessage("Invalid username/password");
                    errorRequest.setErrorValue(userName + "/XXXXXX");
                    errorRequest.setErrorException(null);
                    errorRequest.setErrorType("INVALID_USER_AUTH");
                    errorRequest.setErrorRecoverable(true);

                    errorsRequest.addError(errorRequest);

                    logger.debug("Invalid username/password: " + userName + "/ xxxxxx");
                }
            }

        }

        /*
        else {
        InternetAddress[] addresses = null;
        String userEmail = request.getParameter("paramUserNewEmail");
        try {
        boolean strict = true;
        addresses = InternetAddress.parse(userEmail, strict);
        } catch (AddressException ex) {
        ErrorRequest errorRequest = new ErrorRequest();
        errorRequest.setErrorFormFieldLabel("Form field: Email");
        errorRequest.setErrorMessage("Invalid Email address (1)");
        errorRequest.setErrorValue(userEmail);
        errorRequest.setErrorException(null);
        errorRequest.setErrorType("INVALID_USER_EMAIL");
        errorRequest.setErrorRecoverable(true);
        errorsRequest.addError(errorRequest);
        logger.error("InternetAddress.parse(" + userEmail + ")", ex);
        //ex.printStackTrace();
        }
        rrsRequest.setUserStatus("New user");
        userInfo = new RequestUser();
        userInfo.setAddress(request.getParameter("paramUserNewAddress"));
        userInfo.setAffiliation(request.getParameter("paramUserNewAffiliation"));
        userInfo.setEmail(request.getParameter("paramUserNewEmail"));
        userInfo.setFax(request.getParameter("paramUserNewFax"));
        userInfo.setFirstName(request.getParameter("paramUserNewFirstName"));
        userInfo.setLastName(request.getParameter("paramUserNewLastName"));
        userInfo.setMiddleName(request.getParameter("paramUserNewMiddleName"));
        userInfo.setPhone(request.getParameter("paramUserNewPhone"));
        userInfo.setStatus(request.getParameter("paramUserNewStatus"));
        userInfo.setUserName(request.getParameter("paramUserNewUserName"));
        }
         */

        request.setAttribute("user", userInfo);

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

        rrsRequest.setUser(userInfo);
        rrsRequest.setFromDate(fromDate);
        rrsRequest.setToDate(toDate);

        rrsRequest.setRemarksOther(request.getParameter("paramRequestRemarksOther"));
        rrsRequest.setPublicationAim(request.getParameter("paramRequestPublicationAim"));
        rrsRequest.setResearchProject(request.getParameter("paramRequestResearchProject"));

        String[] values = request.getParameterValues("nodeid");

        AmsLicense amsLicence = new AmsLicense();
        if (values != null) {
            rrsRequest.setNodesEnteredInForm(false);
            if (values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    if (values[i] != null && !(values[i].equalsIgnoreCase(""))) {
                        logger.info("Param values: " + values[i]);
                        ImdiNode imdiNode = new ImdiNode();
                        imdiNode.setImdiNodeIdWithPrefix(values[i]);
                        
                        NodeID nodeId = AmsServicesSingleton.getInstance().getFabricSrv().newNodeID(imdiNode.getImdiNodeIdWithPrefix());
                        logger.info(amsLicence.getLicenseInfo(userName, nodeId));
                        
                        //imdiNode.setImdiNodeName(corpusDbConnection.getNode(values[i]).getName());
                        //imdiNode.setImdiNodeFormat(corpusDbConnection.getNode(values[i]).getFormat());

                        imdiNodes.addImdiNode(imdiNode);

                    }

                }
            }
        } 
        
        /*
         else {
            rrsRequest.setNodesEnteredInForm(true);

            String maxFormNodeIdsString = (String) this.getServletContext().getAttribute("maxFormNodeIds");
            logger.debug("maxFormNodeIdsString: " + maxFormNodeIdsString);
            int maxFormNodeIds = 5;

            try {
                maxFormNodeIds = Integer.parseInt(maxFormNodeIdsString);
            } catch (NumberFormatException ex) {
                logger.error("RrsServlet:Integer.parseInt(" + maxFormNodeIdsString + ")", ex);
            //ex.printStackTrace();
            }

            for (int i = 0; i < maxFormNodeIds; i++) {
                String paramNodeId = "paramNodeId_" + i;
                String nodeId = request.getParameter(paramNodeId);

                if (nodeId != null) {

                    ImdiNode imdiNode = new ImdiNode();
                    imdiNode.setImdiNodeIdWithPrefix(nodeId);

                    if (imdiNode.getImdiNodeIdWithPrefix() != null) {
                        logger.debug("imdiNode.getImdiNodeIdWithPrefix() c: " + imdiNode.getImdiNodeIdWithPrefix());
                        //imdiNode.setImdiNodeName(corpusDbConnection.getNode(imdiNode.getImdiNodeIdWithPrefix()).getName());

                        imdiNodes.addImdiNode(imdiNode);
                        logger.debug("imdiNode added: " + nodeId);
                        logger.debug("imdiNode added prefix: " + imdiNode.getImdiNodeIdWithPrefix());
                    }
                }
            }
        }
        */
        
        for (int i = 0; i < imdiNodes.getSize(); i++) {
            ImdiNode imdiNode = imdiNodes.getImdiNode(i);
            String nodeIdWithPrefix = imdiNode.getImdiNodeIdWithPrefix();
            if (nodeIdWithPrefix != null) {

                try {
                    imdiNode.setImdiNodeName(corpusDbConnection.getNode(nodeIdWithPrefix).getName());
                    imdiNode.setImdiNodeFormat(corpusDbConnection.getNode(nodeIdWithPrefix).getFormat());
                    imdiNode.setImdiNodeUrl(corpusDbConnection.getNamePath(nodeIdWithPrefix));
                    imdiNode.setImdiNodeUri(corpusDbConnection.getObjectURI(nodeIdWithPrefix).toString());

                    imdiNodes.setImdiNode(i, imdiNode);

                } catch (UnknownNodeException ex) {
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

                //ex.printStackTrace();
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

        request.setAttribute("rrsRequest", rrsRequest);

        dispatchServlet(request, response, errorsRequest, rrsRequest);
        return;
    }

    private void dispatchServlet(HttpServletRequest request, HttpServletResponse response, ErrorsRequest errorsRequest, RrsRequest rrsRequest)
            throws ServletException, IOException {

        logger.debug("errorsRequest.getSize(): " + errorsRequest.getSize());

        if (errorsRequest.getSize() > 0) {

            if (errorsRequest.isErrorRecoverable()) {

                errorsRequest.setErrorsHtmlTable();

                String htmlErrorTable = errorsRequest.getErrorsHtmlTable();

                request.setAttribute("htmlErrorTable", htmlErrorTable);

                logger.info("errorsRequest.isErrorRecoverable:" + errorsRequest.isErrorRecoverable() + " call view/error/error.jsp");
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
                view.forward(request, response);
                return;

            } else {
                logger.info("NOT errorsRequest.isErrorRecoverable:" + errorsRequest.isErrorRecoverable() + " call view/error/errorUnknown.jsp");
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/errorUnknown.jsp");
                view.forward(request, response);
                return;
            }

        //throw new mpi.rrs.model.errors.RrsGeneralException(errorsRequest.getErrorsHtmlTable());
        } else {

            EmailBean emailer = new EmailBean();

            rrsRequest.setEmailContent();

            emailer.setSubject("Resource Request System");

            emailer.setContent(rrsRequest.getEmailContent());
            String corpmanEmail = (String) this.getServletContext().getAttribute("emailAddressCorpman");
            String emailHost = (String) this.getServletContext().getAttribute("emailHost");
            String userEmail = rrsRequest.getUser().getEmail();

            emailer.setTo(corpmanEmail);
            emailer.setCc(userEmail);
            emailer.setFrom(corpmanEmail);
            emailer.setSmtpHost(emailHost);
            logger.info("From: " + corpmanEmail);


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

            logger.info("RrsServlet: No Exception");
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/page/result.jsp");
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
    public java.lang.String getServletInfo() {
        return "Resource Request System Controller Servlet";
    }
    // </editor-fold>
}
