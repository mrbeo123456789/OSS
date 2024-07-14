package com.oss.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toEmail);
            helper.setSubject("Please verify your registration");
            helper.setText("<h3><a href=\"http://localhost:8080/verify?email=" + toEmail + "&code=" + verificationCode + "\">click here to verify</a></h3>", true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } public void sendVerificationEmail(String toEmail, String verificationCode, String password) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toEmail);
            helper.setSubject("Please verify your registration");
            helper.setText(
                    "<h3><a href=\"http://localhost:8080/verify?email=" + toEmail + "&code=" + verificationCode + "\">Click here to verify</a></h3>" +
                            "<p>Your password: " + password + "</p>",
                    true
            );

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

