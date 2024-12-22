package com.example.mailing.Controllers;

import com.example.mailing.entity.MailRequest;
import com.example.mailing.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")

    public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest) {
        try {
            mailService.sendEmail(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getTemplateName(), mailRequest.getVariables());
            return ResponseEntity.ok("Email sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}