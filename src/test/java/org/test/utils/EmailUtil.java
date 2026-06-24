package org.test.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtil {

    public static void sendReport() {

        final String fromEmail = "pizza.automation.reports@gmail.com";
        final String password = "bxjwbcwccnomxoyj";
        final String toEmail = "echan@vertere-gs.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject("Test Report");

            MimeBodyPart body = new MimeBodyPart();
            body.setText("Attached is the test report.");

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File("test-output/ExtentReport.html"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);
            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("✅ Email sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}