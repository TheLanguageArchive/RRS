<%@page import="java.util.*" %>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >

    <head>
        <%@include file="/WEB-INF/javascript/validateRequest.js" %>
        <%@include file="/WEB-INF/include/headMeta.jspf" %>
        <link rel="stylesheet" type="text/css" href="css/request.css" />
        <title>ResourceRequest V1</title>
    </head>

    <body>
        <%@include file="/WEB-INF/include/headerTextRequest.jspf" %>
        <hr />

        <form id="resource_request_form_0" method="post" action="RrsIndex2">

            <c:if test="${htmlSelectedNodesTable ne ''}">
                <p class="strong">Your selected Nodes:</p>

                <table>
                    <tr> 
                        <td>Node id 
                        </td>
                        <td>Node name 
                        </td>
                    </tr>

                    ${htmlSelectedNodesTable}

                </table>

                <hr />
            </c:if>


            <p>&nbsp;</p>
            <c:choose>
                <c:when test="${empty uid}">
		    <%-- User is not logged in --%>
		    <p>
			<strong>Do you have an account?</strong><br>
			Only registered users with username and password can request resource access!<br/><br />
			If you do have a "User Id" and "Password", click the button below to login and request access.<br><br>
			<input value="Login to request access" type="submit">
		    </p>
		    <hr />
		    <p>
			If you don't have an account, please register <a href="${urlRrsRegistration}" target ="_blank">here</a>.
		    </p>
		</c:when>
		<c:otherwise>
		    <%-- User is logged in but not registered (otherwise he would not be directed to this page) --%>
		    <p class="strong">
			Only registered users can request resource access.<br />
			We have detected that you are logged in as <em>${uid}</em> but have not registered yet.<br />
			If you would like to request access to there resources, please register <a href="${urlRrsRegistration}" target ="_blank">here</a> first.
		    </p>
		</c:otherwise>
	    </c:choose>
	</form>

    </body>
</html>
