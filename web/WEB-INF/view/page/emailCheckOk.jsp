<%-- 
    Document   : emaiCheckOk
    Created on : May 14, 2008, 3:03:08 PM
    Author     : kees
--%>
<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >
    <head>
    <%@include file="/WEB-INF/include/headMeta.jspf" %>
    <title>ResourceRequest Registration</title>
    <link rel="stylesheet" type="text/css" href="css/request.css">
    </head>
    
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>
        <h2>Thank you for your registration.</h2>
        <hr />
        
        <div id="contents-emailcheck">
            <p>Dear ${userFirstName} ${userLastName}, </p>
            <p>
                Your account has now been created. <br />
                It may take up to 1 hour before it becomes active. <br />
                Note that for many resources, permission from the depositor <br />
                is required before you are allowed to access them. <br />
                Use the &quot;request resource access&quot; function in the IMDI browser <br />
                if you want to access those resources. <br />
                <br />
                You will receive an email on the address: ${userEmail} with the account details.
            </p>
            <p>
                To modify your account details, please go to the page below, log in and select <br />
                &quot;My Account&quot; -> &quot;My UserAccount&quot; from the menu: <br />
                <br />
                <a href="${initParam.AMS_INTERFACE_LINK}" onclick="window.open(this.href); return false;">Access Management System</a>
            </p>
        </div>
        
    </body>
</html>