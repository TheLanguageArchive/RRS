package mpi.rrs.model.email;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import mpi.rrs.model.utilities.RrsUtil;

public class EmailBean  {
    static Logger logger = Logger.getLogger(EmailBean.class);
    
    //defaults
    private final static String DEFAULT_CONTENT = "Unknown content";
    private final static String DEFAULT_SUBJECT= "Unknown subject";
    private static String DEFAULT_SERVER = null;
    private static String DEFAULT_TO = null;
    private static String DEFAULT_FROM = null;
    static{
        java.util.ResourceBundle bundle =
                java.util.ResourceBundle.getBundle("mpi.rrs.model.email.mailDefaults");
        
        DEFAULT_SERVER = bundle.getString("DEFAULT_SERVER");
        DEFAULT_TO = bundle.getString("DEFAULT_TO");
        DEFAULT_FROM = bundle.getString("DEFAULT_FROM");
        
        logger.debug("DEFAULT_SERVER: " + DEFAULT_SERVER);
    }
    
    //JavaBean properties
    private String smtpHost;
    private String to;
    private String cc;
    private String from;
    private String content;
    private String subject;
    
    public void sendMessage() throws Exception {
        
        Properties properties = System.getProperties();
        
        //populate the 'Properties' object with the mail
        //server address, so that the default 'Session'
        //instance can use it.
        properties.put("mail.smtp.host", smtpHost);
        
        Session session = Session.getDefaultInstance(properties);
        
        Message mailMsg = new MimeMessage(session);//a new email message
        
        InternetAddress[] addresses = null;
        
        try {
            
            
            if (to != null) {
                
                //throws 'AddressException' if the 'to' email address
                //violates RFC822 syntax
                addresses = InternetAddress.parse(to, false);
                
                mailMsg.setRecipients(Message.RecipientType.TO, addresses);
                
                logger.debug("EmailBean.sendMessage to: " + to);
                
            } else {
                
                throw new MessagingException(
                        "The mail message requires a 'To' address.");
                
            }
            
            if (cc != null) {
                
                //throws 'AddressException' if the 'to' email address
                //violates RFC822 syntax
                addresses = InternetAddress.parse(cc, false);
                
                mailMsg.setRecipients(Message.RecipientType.CC, addresses);
                
                logger.debug("EmailBean.sendMessage cc: " + cc);
                
            } else {
                
                logger.error("The mail message has no 'Cc' address.");
                
            }
            
            if (from != null) {
                
                mailMsg.setFrom(new InternetAddress(from));
                logger.debug("EmailBean.sendMessage from: " + from);
                
            } else {
                
                throw new MessagingException(
                        "The mail message requires a valid 'From' address.");
                
            }
            
            if (subject != null)
                mailMsg.setSubject(subject);
            
            if (content != null)
                mailMsg.setText(content);
            
            //Finally, send the mail message; throws a 'SendFailedException',
            // com.sun.mail.smtp.SMTPAddressFailedException
            //if any of the message's recipients have an invalid address
            Transport.send(mailMsg);
            
        } catch (Exception exc) {
            logger.error("Address exption from: " + from + " cc: " + cc);
            
            throw exc;
            
        }
        
    }//sendMessage
    
    public void setSmtpHost(String host){
        if (RrsUtil.isNotEmpty(host)){
            this.smtpHost = host;
        } else {
            this.smtpHost = EmailBean.DEFAULT_SERVER;
        }
    }//setTo
    
    public void setTo(String to){
        if (RrsUtil.isNotEmpty(to)){
            this.to = to;
        } else {
            this.to = EmailBean.DEFAULT_TO;
        }
    }//setTo
    
    public void setFrom(String from){
        if (RrsUtil.isNotEmpty(from)){
            this.from = from;
        } else {
            this.from = EmailBean.DEFAULT_FROM;
        }
    }//setFrom
    
    public void setContent(String content){
        if (RrsUtil.isNotEmpty(content)){
            this.content = content;
        } else {
            this.content = EmailBean.DEFAULT_CONTENT;
        }
    }//setContent
    
    public void setSubject(String subject){
        if (RrsUtil.isNotEmpty(subject)){
            this.subject = subject;
        } else {
            this.subject = EmailBean.DEFAULT_SUBJECT;
        }
    }//setSubject

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        if (RrsUtil.isNotEmpty(cc)){
            this.cc = cc;
        } else {
            this.cc = null;
        }
    }
}
