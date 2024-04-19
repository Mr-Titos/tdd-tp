package tdd.tp.utils;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailUtils {
    public static void send(SimpleMailMessage msg) throws MailException {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        mailSender.send(msg);
    }
}
