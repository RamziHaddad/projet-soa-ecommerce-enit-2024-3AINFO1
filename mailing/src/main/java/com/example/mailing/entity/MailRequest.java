package com.example.mailing.entity;

import java.util.Map;

public class MailRequest {
    private String to;               // Le destinataire
    private String subject;          // Le sujet de l'email
    private String templateName;     // Le nom du modèle d'email à utiliser
    private Map<String, String> variables;  // Les variables à remplacer dans le modèle

    // Getters et Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}

