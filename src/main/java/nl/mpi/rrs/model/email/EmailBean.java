package nl.mpi.rrs.model.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nl.mpi.rrs.model.utilities.RrsUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailBean {

    private final static Log logger = LogFactory.getLog(EmailBean.class);
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

            if (subject != null) {
                mailMsg.setSubject(subject);
            }

            if (content != null) {
                mailMsg.setText(content);
            }

            //Finally, send the mail message; throws a 'SendFailedException',
            // com.sun.mail.smtp.SMTPAddressFailedException
            //if any of the message's recipients have an invalid address
            Transport.send(mailMsg);

        } catch (Exception exc) {
            logger.error("Address exception from: " + from + " cc: " + cc);

            throw exc;

        }

    }//sendMessage

    public void setSmtpHost(String host) {
        if (RrsUtil.isNotEmpty(host)) {
            this.smtpHost = host;
        } else {
            throw new IllegalArgumentException("SMTP host cannot be empty");
        }
    }//setTo

    public void setTo(String to) {
        if (RrsUtil.isNotEmpty(to)) {
            this.to = to;
        } else {
            throw new IllegalArgumentException("E-mail 'to' cannot be empty");
        }
    }//setTo

    public void setFrom(String from) {
        if (RrsUtil.isNotEmpty(from)) {
            this.from = from;
        } else {
            throw new IllegalArgumentException("E-mail 'from' cannot be empty");
        }
    }//setFrom

    public void setContent(String content) {
        if (RrsUtil.isNotEmpty(content)) {
            this.content = content;
        } else {
            throw new IllegalArgumentException("E-mail content cannot be empty");
        }
    }//setContent

    public void setSubject(String subject) {
        if (RrsUtil.isNotEmpty(subject)) {
            this.subject = subject;
        } else {
            throw new IllegalArgumentException("E-mail subject cannot be empty");
        }
    }//setSubject

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        if (RrsUtil.isNotEmpty(cc)) {
            this.cc = cc;
        } else {
            this.cc = null;
        }
    }
}
