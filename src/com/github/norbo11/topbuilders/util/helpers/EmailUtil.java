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
        //Create properties for the connection
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); //Attempt to authenticate the user
        props.put("mail.smtp.starttls.enable", "true"); //Enables the use of the STARTTLS command (ensures an encrypted connection before logging in)
       
        //Use Google SMTP server
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Get a session instance, with a new Authenticator object, which will authenticate with the desires username/password (as in the above constants)
        Session session = Session.getInstance(props, new Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(USERNAME, PASSWORD);
           }
        });

        try {
           MimeMessage message = new MimeMessage(session); //Create the message to be sent, with the earlier created session
           
           //Set header fields
           message.setFrom(new InternetAddress(EMAIL_ADDRESS, EMAIL_NAME)); //From the Top Builders e-mail address, with the defined full name to be displayed
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); //To the desired e-mail address
           
           //Set subject and message content
           message.setSubject(subject);
           message.setContent(text, "text/html; charset=utf-8"); //Enable HTML in message, with UTF-8 encoding
           
           //Send message
           Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            //If anything goes wrong, just print an error to the console
            e.printStackTrace();
        }
    }
}
