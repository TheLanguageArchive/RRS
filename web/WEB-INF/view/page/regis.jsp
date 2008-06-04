<%-- 
    Document   : registration
    Created on : Apr 21, 2008, 10:32:32 AM
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
        
        <c:if test="${rrsRegisErrorMessage != null}">
            <script type="text/javascript">
                alert("${rrsRegisErrorMessage}");
            </script>
        </c:if>
        
        <h2>${rrsRegisErrorMessage} &nbsp;</h2>
                  
        <form id="resource_request_registration_form" method="post" action="RrsRegis.do" onsubmit="return validateForm( this )">
            <table id="regis">
                <tr> 
                    <td>User ID (choose one yourself):</td>
                    <td><input type="text" id="paramUserNewUserName" name="paramUserNewUserName" value="${paramUserNewUserName}" size="90" /></td>
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
                
                <tr> 
                    <td>Password (choose one yourself):</td>
                    <td><input type="password" id="paramUserNewPassword_1" name="paramUserNewPassword_1" value="${paramUserNewPassword_1}" size="90" /></td>
                </tr>
                
                <tr> 
                    <td>Password (Enter again for verification):</td>
                    <td><input type="password" id="paramUserNewPassword_2" name="paramUserNewPassword_2" value="${paramUserNewPassword_2}" size="90" /></td>
                </tr>
                
            </table>
            
            <p />
            
            <div id="buttons"> 
                <input type="submit" value="Submit request" />
            </div>
            
        </form>
        
    </body>
</html>
