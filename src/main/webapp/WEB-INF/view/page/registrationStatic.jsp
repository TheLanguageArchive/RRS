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
                        <p>
                            <strong>
                                In order to request a user account, please send an e-mail to the 
                                <a href="mailto:${corpmanEmail}?subject=[RRS]%20Request%20to%20create%20user%20account%20for%20the%20archive&body=Name:%20%0D%0AE-mail:%20%0D%0AOrganization:%20%0D%0ARemarks:%20%0d%0A"/>archive manager</a>
                                in which you provide the following information:<br /><br />
                                - Full name<br />
                                - E-mail address<br />
                                - Organization<br />
                            </strong>
                        </p>
                    </td>
                </tr>
            </table>

            <p />
        </form>

    </body>
</html>
