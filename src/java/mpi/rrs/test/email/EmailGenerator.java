/*
 * EmailGenerator.java
 *
 * Created on March 8, 2007, 4:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mpi.rrs.test.email;

/**
 *
 * @author kees
 */

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.*;
import mpi.rrs.model.email.*;


public class EmailGenerator {
    
    /** Creates a new instance of EmailGenerator */
    public EmailGenerator() {
    }
    
    public void sendRequest() throws ServletException{
    EmailBean emailer = new EmailBean();
    
    emailer.setSubject("Resource Request System");
    emailer.setContent("Please call ASAP.");
    
    emailer.setTo("kees@mpi.nl");
    emailer.setFrom("kees@mpi.nl");
    emailer.setSmtpHost("smtphost.mpi.nl");
    
    
    try{
        emailer.sendMessage();
    } catch (Exception e) {throw new ServletException(e);}
    
    }
    
}
