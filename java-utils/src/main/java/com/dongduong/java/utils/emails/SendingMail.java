/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongduong.java.utils.emails;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dongduong.java.utils.ApplicationConfig;
import com.dongduong.java.utils.WebUtil;
import com.sun.mail.util.MailSSLSocketFactory;

import java.io.InputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author dongduong
 */
public class SendingMail extends WebUtil {

    public boolean send(String filePath, String title, String msg, String to) throws IOException {
        logger.log(Level.OFF, ">>>> Email send {0}", to);
        msg = msg.replace("::", ":");
        msg = msg.replace("??", "?");
        msg = msg.replace("? ?", "?");
        msg = msg.replace("..", ".");
        msg = msg.replace(". .", ".");
        msg = msg.replace(": :", ":");
        msg = msg.replace("。.", ".");
        msg = msg.replace("。 .", ".");
        msg = msg.replace(".:", ":");
        msg = msg.replace(". :", ":");
        msg = msg.replace("。:", ":");
        msg = msg.replace("。 :", ":");

        Transport transport = null;
        try {

            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException ex) {
                logger.log(Level.SEVERE, "GeneralSecurityException {0} ", ex);
            }

            Properties props = new Properties();

            final Properties properties = getPropertiesForSendMail(filePath);

            props.put("mail.smtp.host", properties.getProperty("mail.host"));
            props.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
            props.put("mail.debug", properties.getProperty("mail.debug"));
            props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));
            props.put("mail.smtp.socketFactory.port", properties.getProperty("mail.smtp.socketFactory.port"));

            props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
            props.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {

                String from = properties.getProperty("mail.from");
                String pass = properties.getProperty("mail.password");

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            from, pass);
                }
            });

            session.setDebug("true".equals(properties.getProperty("mail.debug")));// SHOW DEBUG = FALSE
            transport = session.getTransport();
            String emailTiltle = properties.getProperty("mail.title");
            if (WebUtil.nullOrEmpty(emailTiltle)) {
                logger.log(Level.SEVERE, "ERROR : SEND EMAIL MISS CONFIG mail.title");
                emailTiltle = "Push Innovation";
            }
            InternetAddress addressFrom = new InternetAddress(properties.getProperty("mail.from"), emailTiltle);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(addressFrom);
            // create mail
            message.setSubject(title, "UTF-8");
            message.setSentDate(new Date());
            message.setText(msg);
            String sendTo[] = {to};
            message.setSentDate(new Date());
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.saveChanges();
            if (sendTo != null) {
                InternetAddress[] addressTo = new InternetAddress[sendTo.length];
                for (int i = 0; i < sendTo.length; i++) {
                    addressTo[i] = new InternetAddress(sendTo[i]);
                }
                message.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
            }
            transport.connect();
            Transport.send(message);
            logger.log(Level.OFF, ">>>> Email send {0} success", to);
        } catch (SendFailedException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException ex) {
                logger.log(Level.SEVERE, "send email ex {0}", ex);
            }
        }
        return true;
    }

    public boolean sendAttachmentImage(String filePath, String title, String msg, String to, byte[] byteImage, String logoName) throws IOException {
        logger.log(Level.OFF, ">>>> Email send {0}", to);
        msg = msg.replace("::", ":");
        msg = msg.replace("??", "?");
        msg = msg.replace("? ?", "?");
        msg = msg.replace("..", ".");
        msg = msg.replace(". .", ".");
        msg = msg.replace(": :", ":");
        msg = msg.replace("。.", ".");
        msg = msg.replace("。 .", ".");
        msg = msg.replace(".:", ":");
        msg = msg.replace(". :", ":");
        msg = msg.replace("。:", ":");
        msg = msg.replace("。 :", ":");
        Transport transport = null;
        InputStream in = null;
        try {

            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException ex) {
                logger.log(Level.SEVERE, " {0} ", ex);
            }

            Properties props = new Properties();

            final Properties properties = getPropertiesForSendMail(filePath);

            props.put("mail.smtp.host", properties.getProperty("mail.host"));
            props.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
            props.put("mail.debug", properties.getProperty("mail.debug"));
            props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));
            props.put("mail.smtp.socketFactory.port", properties.getProperty("mail.smtp.socketFactory.port"));

            props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
            props.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {

                String from = properties.getProperty("mail.from");
                String pass = properties.getProperty("mail.password");

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            from, pass);
                }
            });

            session.setDebug("true".equals(properties.getProperty("mail.debug")));// SHOW DEBUG = FALSE
            transport = session.getTransport();
            String emailTiltle = properties.getProperty("mail.title");
            if (WebUtil.nullOrEmpty(emailTiltle)) {
                logger.log(Level.SEVERE, "ERROR : SEND EMAIL MISS CONFIG mail.title");
                emailTiltle = "Push Innovation";
            }
            InternetAddress addressFrom = new InternetAddress(properties.getProperty("mail.from"), emailTiltle);
            MimeMessage message = new MimeMessage(session);
            Multipart multiContentPart = new MimeMultipart("alternative");
            Multipart rootBodyPart = new MimeMultipart();

            // Create plain text part
            BodyPart plainMessageBodyPart = new MimeBodyPart();
            plainMessageBodyPart.setContent("Content-Type", "text/plain");
            multiContentPart.addBodyPart(plainMessageBodyPart);

            // Create html part
            BodyPart htmlMessageBodyPart = new MimeBodyPart();
            htmlMessageBodyPart.setContent(msg, "text/html; charset=UTF-8");
            multiContentPart.addBodyPart(htmlMessageBodyPart);

            // Create attachments
            BodyPart attachmentBodyPart = new MimeBodyPart();

            DataSource source = new ByteArrayDataSource(byteImage, "image/*");
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(logoName);
            attachmentBodyPart.setHeader("Content-ID", "<logoImage>");
            attachmentBodyPart.setDisposition(Part.INLINE);
            rootBodyPart.addBodyPart(attachmentBodyPart);

            // Build content
            BodyPart contentWrapper = new MimeBodyPart();
            contentWrapper.setContent(multiContentPart);
            rootBodyPart.addBodyPart(contentWrapper);

            message.setFrom(addressFrom);
            // create mail
            message.setSubject(title, "UTF-8");
            message.setSentDate(new Date());
            //message.setText(msg);
            message.setContent(rootBodyPart);
            String sendTo[] = {to};
            message.setSentDate(new Date());

            message.saveChanges();
            if (sendTo != null) {
                InternetAddress[] addressTo = new InternetAddress[sendTo.length];
                for (int i = 0; i < sendTo.length; i++) {
                    addressTo[i] = new InternetAddress(sendTo[i]);
                }
                message.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
            }
            transport.connect();
            Transport.send(message);

            logger.log(Level.OFF, ">>>> Email send {0} success", to);
        } catch (SendFailedException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException ex) {
                logger.log(Level.SEVERE, "send email ex {0}", ex);
            }
            if (in != null) {
                in.close();
            }

        }
        return true;
    }
    
    public boolean send(final ApplicationConfig properties, String title, String msg, String to) throws IOException {
        logger.log(Level.OFF, ">>>> Email send {0}", to);

        msg = msg.replace("::", ":");
        msg = msg.replace("??", "?");
        msg = msg.replace("? ?", "?");
        msg = msg.replace("..", ".");
        msg = msg.replace(". .", ".");
        msg = msg.replace(": :", ":");
        msg = msg.replace("。.", ".");
        msg = msg.replace("。 .", ".");
        msg = msg.replace(".:", ":");
        msg = msg.replace(". :", ":");
        msg = msg.replace("。:", ":");
        msg = msg.replace("。 :", ":");
        
        Transport transport = null;
        try {

            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException ex) {
                logger.log(Level.SEVERE, "GeneralSecurityException {0} ", ex);
            }

            Properties props = new Properties();

            props.put("mail.smtp.host", properties.getProperty("mail.host"));
            props.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
            props.put("mail.debug", properties.getProperty("mail.debug"));
            props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));
            props.put("mail.smtp.socketFactory.port", properties.getProperty("mail.smtp.socketFactory.port"));

            props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
            props.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {

                String from = properties.getProperty("mail.from");
                String pass = properties.getProperty("mail.password");

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            from, pass);
                }
            });

            session.setDebug("true".equals(properties.getProperty("mail.debug")));// SHOW DEBUG = FALSE
            transport = session.getTransport();
            String emailTiltle = properties.getProperty("mail.title");
            if (WebUtil.nullOrEmpty(emailTiltle)) {
                logger.log(Level.SEVERE, "ERROR : SEND EMAIL MISS CONFIG mail.title");
                emailTiltle = "Push Innovation";
            }
            InternetAddress addressFrom = new InternetAddress(properties.getProperty("mail.from"), emailTiltle);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(addressFrom);
            // create mail
            message.setSubject(title, "UTF-8");
            message.setSentDate(new Date());
            message.setText(msg);
            String sendTo[] = {to};
            message.setSentDate(new Date());
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.saveChanges();
            if (sendTo != null) {
                InternetAddress[] addressTo = new InternetAddress[sendTo.length];
                for (int i = 0; i < sendTo.length; i++) {
                    addressTo[i] = new InternetAddress(sendTo[i]);
                }
                message.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
            }
            transport.connect();
            Transport.send(message);
            logger.log(Level.OFF, ">>>> Email send {0} success", to);
        } catch (SendFailedException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, "send email ex {0}", ex);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException ex) {
                logger.log(Level.SEVERE, "send email ex {0}", ex);
            }
        }
        return true;
    }
}
