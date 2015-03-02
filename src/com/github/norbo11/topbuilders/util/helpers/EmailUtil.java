package com.github.norbo11.topbuilders.util.helpers;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    public static final String EMAIL_ADDRESS = "londontopbuilders@gmail.co.uk";
    public static final String EMAIL_NAME = "London Top Builders";
    public static final String USERNAME = "londontopbuilders";
    public static final String PASSWORD = "computing";
    
    public static void sendEmail(String to, String subject, String text) {
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(USERNAME, PASSWORD);
           }
        });

        try {
           Message message = new MimeMessage(session);

           // Set header fields
           message.setFrom(new InternetAddress(EMAIL_ADDRESS, EMAIL_NAME));
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
           message.setSubject(subject);
           message.setText(text);

           // Send message
           Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
              e.printStackTrace();
        }
    }
}
