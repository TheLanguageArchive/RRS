<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include file="/WEB-INF/include/header.jspf" %>
    
    <body>
        <%@include file="/WEB-INF/include/headerText.jspf" %>
              
        Dear ${user.fullName}, <br> 
        <br>
        An email with your request has been sent to the MPI archive manager<br>
        (cc: ${user.email}). <br> 
        <br>
        The archive manager will contact the owner of the data you requested and <br>
        will let you know as soon as possible whether you have been granted access to the data.
        
    </body>
</html>
