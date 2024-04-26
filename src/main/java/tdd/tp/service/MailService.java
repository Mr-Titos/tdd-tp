package tdd.tp.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailService {
    private static MailService session = null;

    private MailService() {}

    public static MailService getSession() {
        if (session == null)
            session = new MailService();
        return session;
    }
    public void sendSimpleMessage(String receiver, String subject, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);

        JavaMailSender mailSender = new JavaMailSenderImpl();
        mailSender.send(message);
    }
}