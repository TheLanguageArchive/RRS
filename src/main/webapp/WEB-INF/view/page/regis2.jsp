<%-- 
    Document   : regis2
    Created on : Dec 9, 2008, 3:17:30 PM
    Author     : kees
--%>

<%@page import="nl.mpi.rrs.controller.RrsLogin"%>
<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %>>

    <head>
        <script type="text/javascript">
<!--

            <%@include file="/WEB-INF/javascript/functions.js" %>
            <%@include file="/WEB-INF/javascript/validateRegis.js" %>
-->
        </script>

        <title>ResourceRequest Registration</title>
        <%@include file="/WEB-INF/include/headMeta.jspf" %>
        <link rel="stylesheet" type="text/css" href="css/request.css" />
    </head>

    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>

        <hr />

        <%--
        <c:if test="${rrsRegisErrorMessage != null}">
            <script type="text/javascript">
                alert("${rrsRegisErrorMessage}");
            </script>
        </c:if>
        --%>

        <h2 class="error">${rrsRegisErrorMessage} &nbsp;</h2>


        <form id="resource_request_registration_form" method="post" action="RrsRegis.do" onsubmit="return validateForm(this)">
            <table id="regis">
                <tr> 
                    <td colspan="2">
                        <p>
                            By registering an account you can get access to certain resources in  the archive. <br />
                            There are different levels of access to resources:<br />
                            <br />
                            - freely accessible resources, no registration is required<br />
                            - resources that are accessible after registration<br />
                            - resources that are available after registration and signing a license<br />
                            - resources that may become accessible after filling out a request form<br />
                            - resources that are not accessible<br />
                            <br />
                            Your user details will not be used for any purpose other than granting  you access to archived material.<br />
                        </p>
                        <hr />
                        <c:if test="${federated}">
                            <c:choose>
                                <c:when test="${empty uid}">
                                    <p>
                                        <c:choose>
                                            <c:when test="${newInternalUser}">
                                                If you have an account with any of the federated organizations, you can try logging in first,
                                                so that your registration will be linked to that account. Click the link below to review the 
                                                list of federated organizations, then select your organization and use your credentials to 
                                                login. If your organization is not in the list, register for a new account using the form 
                                                below.
                                            </c:when>
                                            <c:otherwise>
                                                In order to register, you need to login using an account of any of the federated organizations.
                                                Click the link below to review the list of federated organizations, then select your 
                                                organization and use your credentials to login. If your organization is not in the list, 
                                                please contact the <a href="mailto:${corpmanEmail}">archive manager</a>.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <a href="RrsLogin?redirect=<%= RrsLogin.RedirectLocation.RrsRegistration%>">Click here to log in</a>.
                                </c:when>
                                <c:otherwise>
                                    <p>You are logged in as <strong>${uid}</strong>. If you register using the form below, your registration
                                        will be linked to this account. <br /> If this is not you, or if you would rather register for a new account, please
                                        log out from the archive and come back to this page.
                                    </c:otherwise>
                                </c:choose>
                            <hr />
                        </c:if>
                    </td>
                </tr>
                <c:if test="${newInternalUser || federated && not empty uid}">
                    <tr>
                        <c:choose>
                            <c:when test="${empty uid}">
                                <td>User ID (choose one yourself):</td>
                                <td><input type="text" id="paramUserNewUserName" name="paramUserNewUserName" value="${paramUserNewUserName}" size="90" /></td>
                            </tr>
                            <tr>  
                                <td></td>
                                <td>
                                    The user ID should have a length of 3-30 characters, and can contain only letters, numbers, and
                                    the following characters: _-. It should start with a letter.
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>User ID:</td>
                                <td>${uid}</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <tr> 
                        <td>First name:</td>
                        <td><input type="text" id="paramUserNewFirstName" name="paramUserNewFirstName" value="${paramUserNewFirstName}" size="90" /></td>
                    </tr>

                    <tr> 
                        <td>Last name:</td>
                        <td><input type="text" id="paramUserNewLastName" name="paramUserNewLastName" value="${paramUserNewLastName}" size="90" /></td>
                    </tr>

                    <tr> 
                        <td>Email:</td>
                        <td><input type="text" id="paramUserNewEmail" name="paramUserNewEmail" value="${paramUserNewEmail}" size="90" /></td>
                    </tr>

                    <tr> 
                        <td>Organization:</td>
                        <td><input type="text" id="paramUserNewOrganization" name="paramUserNewOrganization" value="${paramUserNewOrganization}" size="90" /></td>
                    </tr>
                    <c:if test="${empty uid}"> 
                        <tr> 
                            <td>Password (choose one yourself):</td>
                            <td><input type="password" id="paramUserNewPassword_1" name="paramUserNewPassword_1" value="${paramUserNewPassword_1}" size="90" /></td>
                        </tr>

                        <tr> 
                            <td>Password (Enter again for verification):</td>
                            <td><input type="password" id="paramUserNewPassword_2" name="paramUserNewPassword_2" value="${paramUserNewPassword_2}" size="90" /></td>
                        </tr>
                        <tr>  
                            <td></td>
                            <td>
                                The password should have a length of 6-20 characters, and consist of only letters and numbers.<br />
                                The password must contain at least one numeral and at least one letter.
                            </td>
                        </tr>
                    </c:if>
                </c:if>
                <c:if test="${not federated && not newInternalUser}">
                    <tr>
                        <td><strong>WARNING: RRS was configured not to allow any registration! Please check the configuration or contact the <a href="mailto:${corpmanEmail}">administrator</a>!</strong></td>
                    </tr>
                </c:if>
            </table>

            <p />

            <c:if test="${newInternalUser || federated && not empty uid}">
                <div id="buttons"> 
                    <input type="submit" value="Register" />
                </div>
            </c:if>
        </form>

    </body>
</html>
