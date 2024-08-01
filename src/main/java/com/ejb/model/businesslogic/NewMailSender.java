/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.businesslogic;

/**
 *
 * @author Thilini Madagama
 */
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;

/**
 *
 * @author Thilini Madagama
 */
@Stateless
public class NewMailSender {

//    @Resource(mappedName = "mail/wwole")
//    private Session session;
    public NewMailSender() {
    }

    public boolean sendM(String mailto, String subject, String content) throws NamingException {
        InitialContext ctx = new InitialContext();
        Session session = (Session) ctx.lookup("mail/wwole");

        Properties props = session.getProperties();

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("info@javainstitute.org", "Exon-1987");
            }
        });

        MimeMessage msg = new MimeMessage(session);

        try {
//            Address fromMailId = new InternetAddress("info@wwole.com", fromMailName);
            InternetAddress fromMailId = new InternetAddress("info@javainstitute.org", "info@javainstitute.edu.lk");
            Address toMailId = new InternetAddress(mailto);
            msg.setHeader("Content-Type", "text/html; charset=UTF-8");
            msg.setReplyTo(InternetAddress.parse("info@javainstitute.edu.lk"));
            msg.setContent(content, "text/html");
            msg.setFrom(fromMailId);
            msg.setRecipient(Message.RecipientType.TO, toMailId);
            msg.setSubject(subject, "UTF-8");
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
//            logger.error("error sending mail: " + ex.getMessage());

        }
        return true;
    }

    public boolean sendMWithAtt(String mailto, ArrayList<String> attachedFiles) throws NamingException, IOException {
        InitialContext ctx = new InitialContext();
        Session session = (Session) ctx.lookup("java:comp/env/mail/wwole");

        Properties props = session.getProperties();

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("info@javainstitute.org", "Exon-1987");
            }
        });

        MimeMessage msg = new MimeMessage(session);

        try {
            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("Hello, this is a test email.", "text/html");
            multipart.addBodyPart(messageBodyPart);

            for (String file : attachedFiles) {
                MimeBodyPart fileAttachmentPart = new MimeBodyPart();
                fileAttachmentPart.attachFile(new File(file));
                fileAttachmentPart.setFileName(file);
                fileAttachmentPart.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                multipart.addBodyPart(fileAttachmentPart);
            }

            msg.setContent(multipart);

            InternetAddress fromMailId = new InternetAddress("info@javainstitute.org", "info@javainstitute.edu.lk");
            Address toMailId = new InternetAddress(mailto);
            msg.setFrom(fromMailId);
            msg.setRecipient(Message.RecipientType.TO, toMailId);
            msg.setSubject("Test Email", "UTF-8");
            msg.setHeader("Content-Type", "multipart/mixed; boundary=\"boundary\"");
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void sendMWithAtt(ArrayList<String> attachedFiles) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
