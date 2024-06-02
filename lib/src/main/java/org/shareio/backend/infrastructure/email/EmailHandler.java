package org.shareio.backend.infrastructure.email;

import org.shareio.backend.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailHandler {

    @Value("${spring.mail.username}")
    String sender;

    private final JavaMailSender emailSender;

    public EmailHandler(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    public void sendHelpdeskMessage(
            String receiver, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setBcc(sender);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(Const.MESSAGE_START + text + Const.MESSAGE_END);
        emailSender.send(message);

    }
}