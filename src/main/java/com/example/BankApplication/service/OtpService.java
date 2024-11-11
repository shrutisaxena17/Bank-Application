package com.example.BankApplication.service;

import com.example.BankApplication.model.Customer;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;


@Service
public class OtpService {

    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Customer> customerStore = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;


    public String generateOtp(String registrationId) {
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));
        otpStore.put(registrationId, otp);
        return otp;
    }


    public void sendOtpByEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Your OTP Code");
            helper.setText("Dear customer,\n\nYour OTP code is: " + otp + "\n\nBest regards,\nBank Application Team");

            mailSender.send(mimeMessage);
            System.out.println("OTP sent to email: " + email);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public void storeCustomerData(String registrationId, Customer customer) {
        customerStore.put(registrationId, customer);
    }


    public boolean isOtpValid(String registrationId, String otp) {
        return otp.equals(otpStore.get(registrationId));
    }


    public Customer getStoredCustomerData(String registrationId) {
        return customerStore.get(registrationId);
    }

    public void sendEmail(String email, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(mimeMessage);
            System.out.println("Transaction receipt sent to email: " + email);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentData, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            DataSource dataSource = new ByteArrayDataSource(attachmentData, "application/pdf");
            helper.addAttachment(attachmentName, dataSource);

            mailSender.send(message);
            System.out.println("Email sent successfully with attachment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
