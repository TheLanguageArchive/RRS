

<%@include file="/WEB-INF/javascript/validate.js" %>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>


<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>


<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include file="/WEB-INF/include/header.jspf" %>
    
    <body>
        <%@include file="/WEB-INF/include/headerText.jspf" %>
        
        <form name="resource_request_form" method="post" action="Rrs.do" onSubmit="return validateForm( this )">
            
        <c:if test="${htmlSelectedNodesTable ne ''}">
            <p><strong>Your selected Nodes:</strong></p>
            
            <table border="0">
                <tr valign="top"> 
                    <td>Node id 
                    </td>
                    <td>Node name 
                    </td>
                </tr>
                
                ${htmlSelectedNodesTable}
                
            </table>
            
            <hr>
            <br>
        </c:if>
        
        <h1>To request access to one or more resources please enter the following: </h1> 
        <br>
        <p>&nbsp;</p>
        
        
            <h1>If you already have a username and password please only enter your username and password:</h1>
            
            <table width="85%" border="0">
                <tr> 
                    <td width="22%">Username:</td>
                    <td width="60%"><input type="text" name="paramUserOldUserName" size="90"></td>
                </tr>
                <tr> 
                    <td width="22%">Password</td>
                    <td width="60%"><input type="password" name="paramUserOldPassword" size="90"></td>
                </tr>
            </table>
            <h1>If you don't have a username and password please enter your user info here:</h1>
            <table width="85%" border="0">
                <tr> 
                    <td width="22%">First name:</td>
                    <td width="60%"><input type="text" name="paramUserNewFirstName" size="90"></td>
                </tr>
                <tr> 
                    <td>Prefix (like van, de, etc.)</td>
                    <td><input type="text" name="paramUserNewMiddleName" size="90"></td>
                </tr>
                <tr> 
                    <td>Last name:</td>
                    <td><input type="text" name="paramUserNewLastName" size="90"></td>
                </tr>
                <tr> 
                    <td>UserName (choose one yourself):</td>
                    <td><input type="text" name="paramUserNewUserName" size="90"></td>
                </tr>
                <tr> 
                    <td>Affiliation:</td>
                    <td><input type="text" name="paramUserNewAffiliation" size="90"></td>
                </tr>
                <tr> 
                    <td>Status:</td>
                    <td><input type="text" name="paramUserNewStatus" size="90"></td>
                </tr>
                <tr> 
                    <td valign="top">Entire postal address:</td>
                    <td><textarea type="text" name="paramUserNewAddress" rows="4" cols="70"></textarea></td>
                </tr>
                <tr> 
                    <td>Telephone:</td>
                    <td><input type="text" name="paramUserNewPhone" size="90"></td>
                </tr>
                <tr> 
                    <td>Fax:</td>
                    <td><input type="text" name="paramUserNewFax" size="90"></td>
                </tr>
                <tr> 
                    <td>Email:</td>
                    <td><input type="text" name="paramUserNewEmail" size="90"></td>
                </tr>
            </table>
            
            <p>&nbsp;</p>
            
            
            
            <c:if test="${htmlInputNodesTable ne ''}">
                <p><strong>If you want to access resources or all resources under an archive node, please 
                copy and paste nodeid of the archive node(s):</strong></p>
                
                <table width="99%" border="0">
                    <tr valign="top"> 
                        <td width="16%">Node id 
                            <font size="2"> (right click on the resource and choose 'create bookmark to see the information in the main window of the browser)
                            </font>
                        </td>
                    </tr>
                    
                    ${htmlInputNodesTable}
                    
                </table>
                
            </c:if>            
            
            <p>&nbsp;</p>
            <h1>Indicate the aim 
            of the usage:</h1>
            
            <table width="95%" border="0">
                <tr> 
                    <td width="23%" valign="top">Research 
                    project: </td>
                    <td width="77%"><textarea type="text" name="paramRequestResearchProject" rows="4" cols="90"></textarea> </td>
                </tr>
                <tr> 
                    <td valign="top">Publication 
                    aim:</td>
                    <td><textarea type="text" name="paramRequestPublicationAim" rows="4" cols="90"></textarea> 
                    </td>
                </tr>
                <tr> 
                    <td valign="top">Other remarks:</td>
                    <td><textarea type="text" name="paramRequestRemarksOther" rows="4" cols="90"></textarea> </td>
                </tr>
            </table>
            
            <p>&nbsp;</p>
            <p><strong>Indicate the period that you would like to use the resource:</strong></p>
            <table width="40%" border="0">
                <tr valign="top"> 
                    <td width="9%">From:</td>
                    <td width="6%">Day</td>
                    <td width="9%">  
                        ${pulldownDayOfMonthToday}
                    </td>
                    <td width="6%">Month</td>
                    <td width="14%">  
                        ${pulldownMonthOfYearToday}
                    &nbsp;</td>
                    <td width="6%">Year</td>
                    <td width="50%">  
                        ${pulldownYearToday}
                    &nbsp;</td>
                </tr>
                <tr valign="top"> 
                    <td>To:</td>
                    <td>Day</td>
                    <td> 
                        ${pulldownDayOfMonthEnd}
                    </td>
                    <td>Month</td>
                    <td>  
                        ${pulldownMonthOfYearEnd}
                    &nbsp;</td>
                    <td>Year</td>
                    <td>  
                        ${pulldownYearEnd}
                    </td>
                </tr>
            </table>
            <p>&nbsp;</p>
            <p><input type="submit" value="Submit request"><br>
            
        </form>
        
    </body>
</html>
