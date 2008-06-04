<%-- 
    Document   : dobesCoc
    Created on : Apr 25, 2008, 10:12:43 AM
    Author     : kees
    /RRS_V1/dobes_coc_v2.html
    <iframe  height="500" width="98%" 
                 name="coc" src="${initParam.DOBES_COC_LINK}" >
        </iframe>
--%>
<%@include file="/WEB-INF/include/doctypeStrict.jspf" %>
<html <%@include file="/WEB-INF/include/xhtmlNamespaceAttr.jspf" %> >
    
    <head>
    <%@include file="/WEB-INF/javascript/validateCoc.js" %>
    <%@include file="/WEB-INF/include/headMeta.jspf" %>
    <link rel="stylesheet" type="text/css" href="css/request.css" />
    <title>DOBES Code of Conduct, version 2</title>
    </head>
    
    
    <body>
        <%@include file="/WEB-INF/include/headerTextRegis.jspf" %>
        <h2>Sign Dobes Code of Conduct</h2>
        
        <hr />
        <p>In order to get access to data in the DOBES corpora, 
            you will need to sign the DoBES Code of Conduct.<br />
            Please read the Code of Conduct below and tick the "I agree" button
            at the bottom of the page.
        </p>
        
        <div>
            <object 
                id="dobesCoc" name="dobesCoc" width="98%" height="500" data="${urlRrsDobesCoc}"  type="text/html"> 
            </object>       
        </div>
        
        <p />
        
        <form id="resource_request_dobes_coc_form" method="post" action="RrsCoc.do" onsubmit="return validateForm( this )">
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
                    <input type="submit" value="Submit DoBES Code of Conduct" />
                </div>
                
            </fieldset>
            
        </form>
    </body>
</html>
