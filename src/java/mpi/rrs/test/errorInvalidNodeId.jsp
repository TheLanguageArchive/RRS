<%@page isErrorPage="true" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Error Page</title>
    </head>
    
    <body>
        <h2>An Invalid NodeId Error Occurred</h2>
        <strong>Information about the error:</strong><br>
        ${pageContext.exception}
        <br />
        From Attribute: <br>
        ${errorsRequest.errorsHtmlTable}
        <br><br>
        From  errorsRequestInvalidNodeId:<br>
        ${errorsRequestInvalidNodeId.errorsHtmlTable}
        <br><br>
        From  errorsRequestInvalidUserAuth<br>
        ${errorsRequestInvalidUserAuth.errorsHtmlTable}
        
        <%--
        String exMsg   = exception.getMessage();
        String exClass = exception.getClass().getName();
        
        out.println("exClass:" + exClass);
        
        if ( exClass.equals("java.io.IOException") ) {
            //Doe iets als IO Error.
        } else {
            //Doe iets anders als andere Error.
        }
        
        
        
        <br> exClass2B: <%= exClass %>
        
        <br />
         --%>
         
        <%-- 
        Servlet name: <c:out value="${requestScope[\"javax.servlet.error.servlet_name\"]}" />
        <br />
        <br />
        Type of exception: <c:out value="${requestScope[\"javax.servlet.error.exception\"].class.name}" />
        <br />
        <br />
        Request URI: <c:out value="${requestScope[\"javax.servlet.error.request_uri\"]}" />
        <br />
        <br />
        Message: <c:out value="${requestScope[\"javax.servlet.error.exception\"].message}" />
        --%>
    </body>
</html>










