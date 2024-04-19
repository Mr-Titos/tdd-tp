package tdd.tp.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import tdd.tp.utils.MailUtils;

public class MailService {
    private static MailService session = null;

    private MailService() {}

    public static MailService getSession() {
        if (session == null)
            session = new MailService();
        return session;
    }
    public boolean sendSimpleMessage(String receiver, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        try {
            MailUtils.send(message);
        } catch (MailException e) {
            return false;
        }
        return true;
    }
}