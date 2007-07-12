package mpi.rrs.test.email;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.*;
import javax.servlet.http.*;
import mpi.rrs.*;
import mpi.rrs.model.email.*;

public class EmailBeanServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();
        out.println("<html><head><title>Email message sender</title></head><body>");       
        
        EmailBean emailer = new EmailBean();
        
        emailer.setSubject("This is not spam!");
        emailer.setContent("Please call ASAP.");
        
        emailer.setTo("kees@mpi.nl");
        emailer.setFrom("kees@mpi.nl");
        emailer.setSmtpHost("smtphost.mpi.nl");
        
        out.println("<H1>Email has been sent V5!</H1>");
        
        out.println("</body></html>");
        out.flush();
        out.close();
        
        try{
            emailer.sendMessage();
        } catch (Exception e) {throw new ServletException(e);}
        
        
        
        
    } //doGet
    
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            java.io.IOException {
        
        doGet(request, response);
        
    }
    
}//EmailServlet
