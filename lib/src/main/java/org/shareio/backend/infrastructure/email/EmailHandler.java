package org.shareio.backend.infrastructure.email;

import org.shareio.backend.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailHandler {

    @Value("${spring.mail.username}")
    String sender;

    @Autowired
    private JavaMailSender emailSender;

    public void sendHelpdeskMessage(
            String reciever, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setBcc(sender);
        message.setTo(reciever);
        message.setSubject(subject);
        message.setText(Const.messageStart + text + Const.messageEnd);
        emailSender.send(message);

    }
}