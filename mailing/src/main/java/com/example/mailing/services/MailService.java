package com.example.mailing.services;

import com.example.mailing.entity.EmailTemplate;
import com.example.mailing.Repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateRepository templateRepository;

    public void sendEmail(String to, String subject, String templateName, Map<String, String> variables) throws MessagingException {
        if (to == null || to.isEmpty() || subject == null || subject.isEmpty() || templateName == null || templateName.isEmpty()) {
            throw new IllegalArgumentException("Email parameters are missing");
        }

        // Récupérer le modèle d'email depuis la base de données
        EmailTemplate template = templateRepository.findByName(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateName));

        // Remplacer les variables dans le contenu du modèle
        String content = template.getContent();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        // Préparer le message email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);  // Le deuxième paramètre indique que le contenu est du HTML

        // Envoyer l'email
        mailSender.send(message);
    }
}