/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.businesslogic;

import com.sun.mail.smtp.SMTPTransport;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author Lahiru
 */
public class mailsend {
    
    /**
     * Send email using GMail SMTP server.
     *
     * @param username GMail username
     * @param password GMail password
     * @param recipientEmail TO recipient
     * @param ccEmail CC recipient. Can be empty if there is no CC recipient
     * @param title title of the message
     * @param message message to be sent
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the
     * connected state or if the message is not a MimeMessage
     */
    public static void Send1(final String username, final String password, String recipientEmail, String ccEmail, String title, String message, ArrayList<String> attachedFiles) throws AddressException, MessagingException {
        System.out.println("Sending Mail............");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
       
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        props.put("mail.smtps.quitwait", "false");
        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
         */
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress("<" + username + ">"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        if (ccEmail.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }

        InternetAddress ia[] = {new InternetAddress("NEDp <" + username + ">")};

        msg.setReplyTo(ia);

        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart htmlPart = new MimeBodyPart();
        String htmlContent = message;

        msg.setSubject(title);
        htmlPart.setContent(htmlContent, "text/html");
        msg.setSentDate(new Date());

        multipart.addBodyPart(htmlPart);

        if (attachedFiles != null) {
            if (!attachedFiles.isEmpty()) {

                for (String s : attachedFiles) {

                    htmlPart = new MimeBodyPart();
                    String filename = s;
                    DataSource source = new FileDataSource(filename);
                    htmlPart.setDataHandler(new DataHandler(source));
                    htmlPart.setFileName(filename);
                    multipart.addBodyPart(htmlPart);
                }
            }
        }

        msg.setContent(multipart);
        msg.addHeader("Disposition-Notification-To", "thilinimadagama23@gmail.com");

        SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }

    public static void Send(final String username, final String password, String recipientEmail, String ccEmail, String title, String message, byte[] excelFileBytes, String attachmentFileName) throws AddressException, MessagingException {

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();

        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress("Welcome to NEDp <" + username + ">"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        InternetAddress ia[] = {new InternetAddress("NEDp <" + username + ">")};

        msg.setReplyTo(ia);

        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart htmlPart = new MimeBodyPart();
        String htmlContent = message;

        msg.setSubject(title);
        htmlPart.setContent(htmlContent, "text/html");
        msg.setSentDate(new Date());

        multipart.addBodyPart(htmlPart);

        // Add attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(excelFileBytes, "application/vnd.ms-excel")));
        attachmentPart.setFileName(attachmentFileName);
        multipart.addBodyPart(attachmentPart);

        msg.setContent(multipart);
        msg.addHeader("Disposition-Notification-To", "thilinimadagama23@gmail.com");

        SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
        System.out.println("done");
        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }

    public static void Send(String senderEmail, String senderPassword, String recipientEmail, String subject, String body, Object object, String attendanceData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void Send1(String noreplysrilankasoftwarevalleylk, String sLsvnorepjiat2022, String teacherEmail, String weekly_Attendance_Report, String toString, Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void Send1(String noreplysrilankasoftwarevalleylk, String sLsvnorepjiat2022, String teacherEmail, String ccEmails, String string, String subject, String toString, Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

    private mailsend() {
    }

    /**
     * Send email using GMail SMTP server.
     *
     * @param username GMail username
     * @param password GMail password
     * @param recipientEmail TO recipient
     * @param title title of the message
     * @param message message to be sent
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the
     * connected state or if the message is not a MimeMessage
     */
    public static void Send(final String username, final String password, String recipientEmail, String title, String message, ArrayList<String> attachedFiles) throws AddressException, MessagingException {
        GoogleMail.Send(username, password, recipientEmail, "", title, message, attachedFiles);
    }
//    /**
//     * Send email using GMail SMTP server.
//     *
//     * @param username GMail username
//     * @param password GMail password
//     * @param recipientEmail TO recipient
//     * @param ccEmail CC recipient. Can be empty if there is no CC recipient
//     * @param title title of the message
//     * @param message message to be sent
//     * @throws AddressException if the email address parse failed
//     * @throws MessagingException if the connection is dead or not in the
//     * connected state or if the message is not a MimeMessage
//     */
//    public static void Send(final String username, final String password, String recipientEmail, String ccEmail, String title, String message, ArrayList<String> attachedFiles) throws AddressException, MessagingException {
//        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
//
//        // Get a Properties object
//        Properties props = System.getProperties();
//
//        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "587");
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        
//        props.put("mail.smtps.quitwait", "false");
//
//        Session session = Session.getInstance(props, null);
//
//        // -- Create a new message --
//        final MimeMessage msg = new MimeMessage(session);
//
//        // -- Set the FROM and TO fields --
//        msg.setFrom(new InternetAddress("Welcome to NEDp <" + username + ">"));
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
//
//        
//        InternetAddress ia[] = {new InternetAddress("NEDp <" + username + ">")};
//
//        msg.setReplyTo(ia);
//
//        Multipart multipart = new MimeMultipart("alternative");
//
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        String htmlContent = message;
//
//        msg.setSubject(title);
//        htmlPart.setContent(htmlContent, "text/html");
//        msg.setSentDate(new Date());
//
//        multipart.addBodyPart(htmlPart);
//
//        if (attachedFiles != null) {
//            if (!attachedFiles.isEmpty()) {
//
//                for (String s : attachedFiles) {
//
//                    htmlPart = new MimeBodyPart();
//                    String filename = s;
//                    DataSource source = new FileDataSource(filename);
//                    htmlPart.setDataHandler(new DataHandler(source));
//                    htmlPart.setFileName(filename);
//                    multipart.addBodyPart(htmlPart);
//                }
//            }
//        }
//
//        msg.setContent(multipart);
//        msg.addHeader("Disposition-Notification-To", "thilinimadagama23@gmail.com");
//
//        SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
//
//        t.connect("smtp.gmail.com", username, password);
//        t.sendMessage(msg, msg.getAllRecipients());
//        t.close();
//    }
}
