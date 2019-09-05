package com.example.loginscreen;

import android.content.Context;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class Mail extends Authenticator {
    String finalString = "";
    String from = "fallDetector.Notification@iter.es";
    String host = "mail.iter.es";
    MimeMessage message;
    Multipart multiPart;
    String pass = "userIterSA1";
    String port = "25";
    Session session;
    String user = "fade";

    public Mail(String to, String content, Context context) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.port", this.port);
        this.session = Session.getDefaultInstance(props, null);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(this.finalString.getBytes(), "text/plain"));
        this.message = new MimeMessage(this.session);
        this.message.setFrom(new InternetAddress(this.from));
        this.message.setDataHandler(handler);
        this.multiPart = new MimeMultipart();
        this.message.addRecipient(RecipientType.TO, new InternetAddress(to));
        this.message.setSubject(context.getString(R.string.email_subject));
        this.message.setContent(this.multiPart);
        this.message.setText(content);
    }

    public boolean send() throws AddressException, MessagingException {
        try {
            Transport transport = this.session.getTransport("smtp");
            transport.connect(this.host, this.user, this.pass);
            transport.sendMessage(this.message, this.message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
