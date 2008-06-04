<%-- 
    Document   : errorEmailCheck
    Created on : May 15, 2008, 10:39:24 AM
    Author     : kees
--%>

<%@page isErrorPage="true" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >
    
    <head>
    <%@include file="/WEB-INF/include/headMeta.jspf" %>
    <link rel="stylesheet" type="text/css" href="css/error.css" />
    <title>Error page</title>
    </head>
    
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>
        
        <hr />
        
        <div id="contents-erroremailcheck">
            <p>
                Dear user, <br />
                You have clicked on an invalid link!            
            </p>
            
            
            <div class="strong">
                Information about the error(s):
            </div>
            
            <br />
            <br />
            <table>
                <tr>
                    <th>Where</th>
                    <th>Message</th>
                    <th>Error</th>
                </tr>  
                ${htmlErrorTable}
            </table>
            <br />
            <br />
            <p>
                If you don't have a 'User Id' and 'Password' please register <a href="/RRS_V1/RrsRegistration" target="_blank">here</a> 
            </p>
        </div>     
        
    </body>
</html>










