<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="mpi.rrs.model.*" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include file="/WEB-INF/include/header.jspf" %>
    
    <body>
        
        <h1>JSP result Page</h1>
        
        Your request is OK. <BR>
        
        <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
        <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
    --%>
    
        <%
        Enumeration e = request.getParameterNames();
        
        
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = request.getParameter(name);
            String[] values = request.getParameterValues(name);
            
            if (values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    if (values[i] != null && !(values[i].equalsIgnoreCase(""))) {
                        //out.println(name + " == " + values[i] + "<BR>");
                    } else {
                        //out.println(name + "<BR>");
                    }
                    
                }
            } else {
                //out.println(name + " = " + value + "<BR>");
            }
        }
        
        
        
        Enumeration e2 = request.getAttributeNames();
        
        while (e2.hasMoreElements()) {
            String name = (String)e2.nextElement();
            out.println("attribute: " + name + "<BR>");
        }
        
        //RrsRequest rrs = (RrsRequest) request.getAttribute("rrsRequest");
        //out.print(rrs.getUser().toHtmlString());
        //
        %>
        
        requestScope["rrsRequest"] : ${requestScope["rrsRequest"]} <br>
        rrsRequest remarks other : ${rrsRequest.remarksOther} <br>
        rrsRequest user last : ${rrsRequest.user.userName} <br>
        user last : ${user.userName} <br>
        From date: ${rrsRequest.fromDate.myDate} <br>
        To date: ${rrsRequest.toDate.day} -  ${rrsRequest.toDate.month} -  ${rrsRequest.toDate.year} <br>
        
        imdiNodes toString: ${rrsRequest.imdiNodes} <br>
        imdiNodes size: ${rrsRequest.imdiNodes.size}  <br>
        imdiNodes index 0 name: ${rrsRequest.imdiNodes.imdiNodes[0].imdiNodeName} <br>
        imdiNodes index 0 int: ${rrsRequest.imdiNodes.imdiNodes[0].imdiNodeIdAsInt} <br>
        imdiNodes index 0 url : ${rrsRequest.imdiNodes.imdiNodes[0].imdiNodeUrl} <br>
        <br>
        
        <table border="1" color="#ff8080" bgcolor="#ffc0c0" title="Panel 1"">
            ${rrsRequest.imdiNodes.htmlTable}
        </table>
        
    </body>
</html>
