package tdd.exo1.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import tdd.exo1.utils.MailUtils;

public class MailService {

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