<%-- 
    Document   : emaiCheckOk
    Created on : May 14, 2008, 3:03:08 PM
    Author     : kees
--%>

<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >
    <head>
    <%@include file="/WEB-INF/include/headMeta.jspf" %>
    <title>ResourceRequest Registration</title>
    <link rel="stylesheet" type="text/css" href="css/request.css">
    </head>
    
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>
        <h2>Thank you for your registration</h2>
        <hr />
        
        <div id="contents-emailcheck">
            <p>Dear ${userFirstName} ${userLastName}, </p>
            <p>
                Your account has now been created. <br />
                <strong>It may take up to 1 hour before it becomes active.</strong>
            </p>
            <p>
                Note that for many resources, permission from the depositor <br />
                is required before you are allowed to access them. <br />
                Use the &quot;request resource access&quot; function in the IMDI browser <br />
                if you want to access those resources.
	    </p>

	    <c:if test="${not empty nodeIds}">
		<p>
		    You have selected the following nodes:
		    <ul>
		    <c:forEach var="nodeId" items="${nodeIds}">
			<li>${nodeId}</li>
		    </c:forEach>
		    </ul>
		</p>
	    </c:if>
		
	    <p>
                You will receive an email on the address: ${userEmail} with the account details.<br/><br/>
            
                To modify your account details, please go to the page below, log in and select <br />
                &quot;My Account&quot; -> &quot;My UserAccount&quot; from the menu: <br />
                <br />
                <a href="${amsInterfaceLink}" onclick="window.open(this.href); return false;">Access Management System</a>
            </p>
	    <c:if test="${!empty idpName}">
		<p>
		    To login, press the 'login' link in the IMDI browser and select the following identity provider from the list:<br/><br/>
		    <strong>${idpName}</strong>
		</p>
	    </c:if>
        </div>
        
    </body>
</html>