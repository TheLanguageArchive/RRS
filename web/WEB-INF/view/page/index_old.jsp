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
        
        <form id="resource_request_form" method="post" action="Rrs.do" onsubmit="return validateForm( this )">
            
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
            
            
            <p class="strong">Login: (if you don't have a 'User Id' and 'Password' please register <a href="${RrsRegistration}" onclick="window.open(this.href); return false;">here</a>)</p>            
            
            <table>
                <tr> 
                    <td>User ID:</td>
                    <td><input type="text" name="paramUserOldUserName" size="90" /></td>
                </tr>
                <tr> 
                    <td>Password</td>
                    <td><input type="password" name="paramUserOldPassword" size="90" /></td>
                </tr>
            </table>
            
            <p>&nbsp;</p>
            
            <p class="strong">Indicate the aim of the usage:</p>
            
            <table>
                <tr> 
                    <td>Research project: </td>
                    <td><textarea name="paramRequestResearchProject" rows="4" cols="90"></textarea> </td>
                </tr>
                <tr> 
                    <td>Publication aim:</td>
                    <td><textarea name="paramRequestPublicationAim" rows="4" cols="90"></textarea> 
                    </td>
                </tr>
                <tr> 
                    <td>Other remarks:</td>
                    <td><textarea name="paramRequestRemarksOther" rows="4" cols="90"></textarea> </td>
                </tr>
            </table>
            
            <p>&nbsp;</p>
            <p class="strong">Indicate the period that you would like to use the resource:</p>
            
            <table>
                <tr> 
                    <td>From:</td>
                    <td class="align-right">Day</td>
                    <td>  
                        ${pulldownDayOfMonthToday}
                    </td>
                    <td class="align-right">Month</td>
                    <td>  
                        ${pulldownMonthOfYearToday}
                    </td>
                    <td class="align-right">Year</td>
                    <td>  
                        ${pulldownYearToday}
                    </td>
                </tr>
                <tr> 
                    <td>To:</td>
                    <td class="align-right">Day</td>
                    <td> 
                        ${pulldownDayOfMonthEnd}
                    </td>
                    <td class="align-right">Month</td>
                    <td>  
                        ${pulldownMonthOfYearEnd}
                    &nbsp;</td>
                    <td class="align-right">Year</td>
                    <td>  
                        ${pulldownYearEnd}
                    </td>
                </tr>
            </table>
            
            <p>&nbsp;</p>
            
            <p>
                <input type="submit" value="Submit request" />
            </p>
            
        </form>
        
    </body>
</html>