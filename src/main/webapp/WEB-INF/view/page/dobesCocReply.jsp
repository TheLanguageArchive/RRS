<%-- 
    Document   : DobesCocReply
    Created on : Apr 28, 2008, 10:55:18 AM
    Author     : kees
--%>
<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>ResourceRequest Registration</title>
    <link rel="stylesheet" type="text/css" href="css/request.css" />
    </head>
    
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>
        <h2>Email address verification</h2>
        <hr />
        
        <div id="dobes-coc-reply">
            <p>Dear ${userFirstName} ${userLastName}, </p>
            <p />            
            You will receive an email on the address: ${userEmail} containing a link <br />
            on which you need to click in order to verify your email address.
        </div>
        
    </body>
</html>
