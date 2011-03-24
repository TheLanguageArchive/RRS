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
        
        <form id="resource_request_form_0" method="post" action="RrsIndex2">
            
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
            
            
            <p>&nbsp;</p>
            
            <p class="strong">
               Only registered users with username and password can request resource access.<br />
               If you don't have a &quot;User Id&quot; and &quot;Password&quot; 
              please register <a href="${urlRrsRegistration}" onclick="window.open(this.href); return false;">here</a>
              <br />
              Tick the "Continue" button to login.
            </p> 
            
            <input type="hidden" name="fromFirstPage" value="1" />
            
            <%--
            <table>
                
                <tr> 
                    <td>User Id:</td>
                    <td><input type="text" name="paramUserOldUserName" size="90" /></td>
                </tr>
                <tr> 
                    <td>Password</td>
                    <td><input type="password" name="paramUserOldPassword" size="90" /></td>
                </tr>   
                
                
                <tr> 
                    <td colspan="2" class="align-left"><p class="strong">Indicate the aim of the usage:</p></td>
                </tr>
                
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
                                
                <tr> 
                    <td colspan="2" class="align-left"><p class="strong">Indicate the period that you would like to use the resource:</p></td>
                </tr>
                
                <tr> 
                    <td>From:</td>
                    <td>Day: ${pulldownDayOfMonthToday} &nbsp;
                        Month: ${pulldownMonthOfYearToday} &nbsp;
                        Year: ${pulldownYearToday}
                    </td>
                </tr>
                <tr> 
                    <td>To:</td>
                    <td>Day: ${pulldownDayOfMonthEnd} &nbsp;
                        Month: ${pulldownMonthOfYearEnd} &nbsp;
                        Year: ${pulldownYearEnd}
                    </td>
                </tr>
            </table>
            --%>
            <p>
                <input type="submit" value="Continue" />
            </p>
            
        </form>
        
    </body>
</html>
