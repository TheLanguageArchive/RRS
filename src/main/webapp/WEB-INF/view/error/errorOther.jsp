<%-- 
    Document   : errorOther
    Created on : Jun 4, 2008, 12:03:54 PM
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
        <%@include file="/WEB-INF/include/headerErrorText.jspf" %>
        
        <div class="strong">
            Information about the error(s):
        </div>
        
        
        <table>
                <tr>
                    <th>Where</th>
                    <th>Message</th>
                    <th>Error</th>
                </tr>         
                ${htmlErrorTable}
        </table>
        
    </body>
</html>