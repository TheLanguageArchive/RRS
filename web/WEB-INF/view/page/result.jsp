<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>

<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >

    <head>
        <title>ResourceRequest V1</title>
        <%@include file="/WEB-INF/include/headMeta.jspf" %>
        <link rel="stylesheet" type="text/css" href="css/request.css" />
    </head>  
    <body>
        <%@include file="/WEB-INF/include/headerTextRequest.jspf" %>
        <hr />
        <p>
            Dear ${user.fullName}, <br /> 
            <br />
            An email with your request has been sent to the MPI archive manager <br />
            (cc: ${user.email}).<br />
            
            <br />
            
            The archive manager will contact the owner of the data you requested and <br />
            will let you know as soon as possible whether you have been granted access to the data.
        </p>
        
    </body>
</html>
