<%-- 
    Document   : dobesCoc
    Created on : Apr 25, 2008, 10:12:43 AM
    Author     : kees
    /RRS_V1/dobes_coc_v2.html
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >

    <head>
        <script type="text/javascript">
<!--
            <%@include file="/WEB-INF/javascript/validateCoc.js" %>
-- >        
        </script>
        <%@include file="/WEB-INF/include/headMeta.jspf" %>
        <link rel="stylesheet" type="text/css" href="css/request.css" />
        <c:choose>
            <c:when test="${empty urlRrsDobesCoc}">
                <title>Submit registration request</title>		 
            </c:when>
            <c:otherwise>
                <title>DOBES Code of Conduct, version 2</title>	   
            </c:otherwise>
        </c:choose>
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>

        <c:choose>
            <c:when test="${empty urlRrsDobesCoc}">
                <h2>Finish registration</h2>
                <p>
                    You have entered the following data:
                <ul>
                    <li>Username: ${user.userName}</li>
                    <li>First name: ${user.firstName}</li>
                    <li>Last name:  ${user.lastName}</li>
                    <li>E-mail: ${user.email}</li>
                </ul>
            </p>
            <p>
                Click the button below to submit the registration request.
            </p>

            <form id="resource_request_dobes_coc_form" method="post" action="RrsCoc.do" >

                <input type="hidden" name="userName" value="${user.userName}" />
                <input type="hidden" name="userFirstName" value="${user.firstName}" />
                <input type="hidden" name="userLastName" value="${user.lastName}" />
                <input type="hidden" name="userEmail" value="${user.email}" />
                <input type="hidden" name="coc_agree" value="" />

                <div id="buttons"> 
                    <input type="submit" value="Submit request" />
                </div>

            </form>
        </c:when>
        <c:otherwise>


            <h2>Sign Dobes Code of Conduct</h2>

            <hr />
            <p>In order to get access to data in the DOBES corpora, 
                you will need to sign the DoBES Code of Conduct.<br />
                It is recommended that you read the Code of Conduct below and tick the "I agree" button
                at the bottom of the page. <br />You can also choose to skip this step, and optionally accept the
                code of conduct later.
            </p>

            <div>
                <object 
                    id="dobesCoc" name="dobesCoc" width="98%" height="500" data="${urlRrsDobesCoc}"  type="text/html"> 
                </object>       
            </div>

            <p />

            <form id="resource_request_dobes_coc_form" method="post" action="RrsCoc.do" >
                <fieldset>

                    <input type="hidden" name="userName" value="${user.userName}" />
                    <input type="hidden" name="userFirstName" value="${user.firstName}" />
                    <input type="hidden" name="userLastName" value="${user.lastName}" />
                    <input type="hidden" name="userEmail" value="${user.email}" />

                    <div id="i-agree"> 
                        <label for="coc_agree">I agree</label>
                        <input id="coc_agree" type="checkbox" name="coc_agree" value="ON" />
                    </div>

                    <div id="buttons"> 
                        <input onclick="return validateForm(getElementById('resource_request_dobes_coc_form'))" type="submit" value="Submit DoBES Code of Conduct" /><br />
                        <hr />
                        <input type="submit" value="Skip this step" />
                    </div>


                </fieldset>

            </form>
        </c:otherwise>
    </c:choose>
</body>
</html>
