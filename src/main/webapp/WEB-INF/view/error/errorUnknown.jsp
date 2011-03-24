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
        
        <h2>Sorry, <br />
            Resource Request System is out of order! 
        </h2>
        
        <p>
            ${pageContext.exception}
        </p>
        
    </body>
</html>










