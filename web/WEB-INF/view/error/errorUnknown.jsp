<%@page isErrorPage="true" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include file="/WEB-INF/include/header.jspf" %>
    
    <body>
        <%@include file="/WEB-INF/include/headerErrorText.jspf" %>
        
        <h2>Sorry, <br>
            Resource Request System is out of order! <h2>
            ${pageContext.exception}
        <br />
        
    </body>
</html>










