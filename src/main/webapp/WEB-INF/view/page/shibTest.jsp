<%-- 
    Document   : shibTest
    Created on : Apr 5, 2011, 4:47:04 PM
    Author     : Twan Goosen <twan.goosen@mpi.nl>
--%>

<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="MacRoman"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=MacRoman">
        <title>Shibboleth test</title>
    </head>
    <body>
        <h1>Attributes</h1>
        <% HashMap<String, Object> attributesMap = (HashMap<String, Object>) request.getAttribute("attributes"); %>

        <table>
        <% for(String id:attributesMap.keySet()){ %>
            <tr>
                <td><%= id %></td>
                <td><%= attributesMap.get(id) %></td>
            </tr>
        <% } %>
        </table>
    </body>
</html>
