<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



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
        
    <%
            Enumeration e = request.getParameterNames();
            
            
            while (e.hasMoreElements()) {
                String name = (String)e.nextElement();
                String value = request.getParameter(name);
                String[] values = request.getParameterValues(name);
                
                if (values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        if (values[i] != null && !(values[i].equalsIgnoreCase(""))) {
                            out.println(name + " == " + values[i] + "<BR>");
                        } else {
                            out.println(name + "<BR>");
                        }
                        
                    }
                } else {
                    out.println(name + " = " + value + "<BR>");
                }
            }
            
            
            
            Enumeration e2 = request.getAttributeNames();
            
            while (e2.hasMoreElements()) {
                String name = (String)e2.nextElement();
                out.println("attribute: " + name + "<BR>");
            }
            
            
            %>
            
    <h1>To request access to one or more resources please enter the following: </h1>  
    <form name="resource_request_form" method="post" action="Rrs.do">
        <h1>If you already have a username and password please only enter your username and password:</h1>
        
        <table width="85%" border="0">
            <tr> 
                <td width="22%">UserName:</td>
                <td width="60%"><input type="text" name="paramUserOldUserName" size="90"></td>
            </tr>
            <tr> 
                <td width="22%">Password</td>
                <td width="60%"><input type="text" name="paramUserOldPassword" size="90"></td>
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
                <td>e-mail address:</td>
                <td><input type="text" name="paramUserNewEmail" size="90"></td>
            </tr>
        </table>
        
        <p>&nbsp;</p>
        <h1>If you would like to access individual resources, please copy and paste 
        the URL of the requested resource(s):</h1>
        <table width="99%" border="0">
            <tr>
                <td width="16%">Resource name</td>
                <td width="84%">URL <font size="2">(right click on the resource and choose 
                    'create bookmark to see the information in the main window of the browser)</font> 
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <input type="text" name="paramResourceName_1" size="30">
                </td>
                <td><input type="text" name="paramResourceUrl_1" size="140"></td>
            </tr>
            <tr>
                <td valign="top">
                <input type="text" name="paramResourceName_2" size="30"></td>
                <td><input type="text" name="paramResourceUrl_2" size="140"></td>
            </tr>
            <tr>
                <td valign="top"> <input type="text" name="paramResourceName_3" size="30"></td>
                <td><input type="text" name="paramResourceUrl_3" size="140"></td>
            </tr>
            <tr>
                <td valign="top">
                    <input type="text" name="paramResourceName_4" size="30">
                </td>
                <td><input type="text" name="paramResourceUrl_4" size="140"></td>
            </tr>
            <tr>
                <td valign="top">
                <input type="text" name="paramResourceName_5" size="30"></td>
                <td><input type="text" name="paramResourceUrl_5" size="140"></td>
            </tr>
        </table>
        <p><strong>If you want to access all resources under an archive node, please 
        copy and paste the URL of the archive node(s):</strong></p>
        <table width="99%" border="0">
            <tr valign="top"> 
                <td width="16%">Node name </td>
                <td width="84%">URL<font size="2"> (right click on the resource and choose 
                'create bookmark to see the information in the main window of the browser)</font></td>
            </tr>
            <tr> 
                <td valign="top"> <input type="text" name="paramNodeName_1" size="30"></td>
                <td><input type="text" name="paramNodeUrl_1" size="140"></td>
            </tr>
            <tr> 
                <td valign="top">
                <input type="text" name="paramNodeName_2" size="30"></td>
                <td><input type="text" name="paramNodeUrl_2" size="140"></td>
            </tr>
            <tr> 
                <td valign="top">
                <input type="text" name="paramNodeName_3" size="30"></td>
                <td><input type="text" name="paramNodeUrl_3" size="140"></td>
            </tr>
            <tr> 
                <td valign="top">
                <input type="text" name="paramNodeName_4" size="30"></td>
                <td><input type="text" name="paramNodeUrl_4" size="140"></td>
            </tr>
            <tr> 
                <td valign="top">
                <input type="text" name="paramNodeName_5" size="30"></td>
                <td><input type="text" name="paramNodeUrl_5" size="140"></td>
            </tr>
        </table>
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
                    <select name="paramRequestFromDateDay">
                        <option> Choose</option>
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
                        <option>8</option>
                        <option>9</option>
                        <option>10</option>
                        <option>11</option>
                        <option>12</option> 
                        <option>13</option>
                        <option>14</option>
                        <option>15</option>
                        <option>16</option>
                        <option>17</option>
                        <option>18</option>
                        <option>19</option>
                        <option>20</option>
                        <option>21</option>
                        <option>22</option>
                        <option>23</option>
                        <option>24</option>
                        <option>25</option>
                        <option>26</option>
                        <option>27</option>
                        <option>28</option>
                        <option>29</option>
                        <option>30</option>
                        <option>31</option>
                    </select>
                </td>
                <td width="6%">Month</td>
                <td width="14%">  
                    <select name="paramRequestFromDateMonth">
                        <option>Choose...</option>
                        <option>Jan</option>
                        <option>Feb</option>
                        <option>March</option>
                        <option>April</option>
                        <option>Mai</option>
                        <option>June</option>
                        <option>July</option>
                        <option>Aug</option>
                        <option>Sept</option>
                        <option>Oct</option>
                        <option>Nov</option>
                        <option>Dec</option>
                    </select>
                &nbsp;</td>
                <td width="6%">Year</td>
                <td width="50%">  
                    <select name="paramRequestFromDateYear">
                        <option>Choose...</option>
                        <option>2006</option>
                        <option>2007</option>
                    </select>
                &nbsp;</td>
            </tr>
            <tr valign="top"> 
                <td>To:</td>
                <td>Day</td>
                <td> 
                    <select name="paramRequestToDateDay">
                        <option> Choose</option>
                        
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
                        <option>8</option>
                        <option>9</option>
                        <option>10</option>
                        <option>11</option>
                        <option>12</option> 
                        <option>13</option>
                        <option>14</option>
                        <option>15</option>
                        <option>16</option>
                        <option>17</option>
                        <option>18</option>
                        <option>19</option>
                        <option>20</option>
                        <option>21</option>
                        <option>22</option>
                        <option>23</option>
                        <option>24</option>
                        <option>25</option>
                        <option>26</option>
                        <option>27</option>
                        <option>28</option>
                        <option>29</option>
                        <option>30</option>
                        <option>31</option>
                    </select>
                </td>
                <td>Month</td>
                <td>  
                    <select name="paramRequestToDateMonth">
                        <option>Choose...</option>
                        <option>Jan</option>
                        <option>Feb</option>
                        <option>March</option>
                        <option>April</option>
                        <option>Mai</option>
                        <option>June</option>
                        <option>July</option>
                        <option>Aug</option>
                        <option>Sept</option>
                        <option>Oct</option>
                        <option>Nov</option>
                        <option>Dec</option>
                    </select>
                &nbsp;</td>
                <td>Year</td>
                <td>  
                    <select name="paramRequestToDateYear">
                        <option>Choose...</option>
                        <option>2006</option>
                        <option>2007</option>
                        <option>2008</option>
                    </select>
                </td>
            </tr>
        </table>
        <p>&nbsp;</p>
        <p><input type="submit" value="Submit request"><br>
    </form>
    
    
     <%--   
        <h1>JSP 2 Page</h1>
        <form name="param_Send Email" action="http://localhost:8084/RRS_V1/EmailBeanServlet">
            <input type="submit" value="Send Email" name="param_SendEmail" />
        </form>
        
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    
        <c:if test="${param.sayHello}">
            <!-- Let's welcome the user ${param.name} -->
            Hello ${param.name}!
        </c:if>
        
        
    </body>
</html>
