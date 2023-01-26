package com.frost.bfriend.util.certification;

import com.frost.bfriend.constants.EmailConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.frost.bfriend.constants.EmailConstants.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final CertificationGenerator certificationGenerator;

    public String sendCertificationEmail(String email) {
        String code = certificationGenerator.createRandomCode();
        SimpleMailMessage mailMessage = createMailMessage(
                email, CERTIFICATION_TITLE, CERTIFICATION_CONTENT + code);
        javaMailSender.send(mailMessage);

        return code;
    }

    public SimpleMailMessage createMailMessage(String to, String title, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setTo(to);
        mailMessage.setSubject(title);
        mailMessage.setText(content);

        return mailMessage;
    }



    public String sendNewPasswordEmail(String email) {
        String newPassword = certificationGenerator.createRandomPassword();

        SimpleMailMessage mailMessage = createMailMessage(
                email, NEW_PASSWORD_TITLE, NEW_PASSWORD_CONTENT + newPassword);
        javaMailSender.send(mailMessage);

        return newPassword;
    }


}
