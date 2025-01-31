package Utilities;

import Base.TestSetup;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileNotFoundException;
import java.util.Properties;

import static Base.TestSetup.log;
import static Base.FrameX_Listeners.*;
import static Utilities.Constants.*;
import static Utilities.AppUtils.generatedateandtime;


public class Mailconfig {

    private static  String mailpassword = "nvscghckuwobmhiv";

    public static void sendMailReport() throws MessagingException, FileNotFoundException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", TestSetup.properties.get("Host"));
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", TestSetup.properties.get("Port"));
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.ciphers", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.ciphers", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(TestSetup.properties.get("Sendermail"), mailpassword);

                    }
                });
        session.setDebug(false);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(TestSetup.properties.get("Sendermail")));
            String[] recipients = {
                    TestSetup.properties.get("Reciptents1"),
                    TestSetup.properties.get("Reciptents2"),
            };
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO,recipientAddresses);
            message.setSubject( TestSetup.properties.get("Subject"));
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(body);

            // Attach first file
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(allureDirpath + AllureReportfileName);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName("Automation Test Report_"+generatedateandtime()+".html");

            // Attach second file
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart2);
            multipart.addBodyPart(messageBodyPart1);

            message.setContent(multipart);
            Transport.send(message);
            log.info("Email sent");

        } catch (MessagingException e) {
            log.info("Email is not sent");
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }


}

