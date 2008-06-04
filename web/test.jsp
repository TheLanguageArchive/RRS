<%-- 
    Document   : test
    Created on : Apr 24, 2008, 11:01:53 AM
    Author     : kees
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Hello World!</h2>
        <%-- context-param in web.xml: --%>        
        <p>email host: ${initParam.EMAIL_HOST} </p>
        
        <%-- ServletContext attribute: --%>  
        <p>email host: ${emailHost} </p>
        
    </body>
</html>
