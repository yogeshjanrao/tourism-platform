package com.microservices.auth.util;

import com.microservices.auth.exception.AuthServiceException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSenderUtil {

    @Autowired
    private  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;


    public  String sendMail(String to, String subject, String content) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML
            helper.setFrom(fromMail);

            mailSender.send(message);

            return "Successfully send email";

        } catch (Exception ex) {
            throw new AuthServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Enable to send a mail");
        }

    }
}
