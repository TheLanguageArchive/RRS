<%-- 
    Document   : regis2
    Created on : Dec 9, 2008, 3:17:30 PM
    Author     : kees
--%>

<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %>>
    
    <head>
    <%@include file="/WEB-INF/javascript/functions.js" %>
    <%@include file="/WEB-INF/javascript/validateRegis.js" %>
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
        
        <h2>${rrsRegisErrorMessage} &nbsp;</h2>
        
        
        <form id="resource_request_registration_form" method="post" action="RrsRegis.do" onsubmit="return validateForm( this )">
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
                    </td>
                </tr>
                <tr>
                    <% if(request.getAttribute("uid") == null) {%>
                    <td>User ID (choose one yourself):</td>
                    <td><input type="text" id="paramUserNewUserName" name="paramUserNewUserName" value="${paramUserNewUserName}" size="90" /></td>
                    <% } else {%>
                    <td>User ID:</td>
                    <td>${uid}</td>
                    <% } %>
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
                <% if(request.getAttribute("uid") == null) {%>
                <tr> 
                    <td>Password (choose one yourself):</td>
                    <td><input type="password" id="paramUserNewPassword_1" name="paramUserNewPassword_1" value="${paramUserNewPassword_1}" size="90" /></td>
                </tr>
                
                <tr> 
                    <td>Password (Enter again for verification):</td>
                    <td><input type="password" id="paramUserNewPassword_2" name="paramUserNewPassword_2" value="${paramUserNewPassword_2}" size="90" /></td>
                </tr>
                <% } %>
            </table>
            
            <p />
            
            <div id="buttons"> 
                <input type="submit" value="Register" />
            </div>
            
        </form>
        
    </body>
</html>
