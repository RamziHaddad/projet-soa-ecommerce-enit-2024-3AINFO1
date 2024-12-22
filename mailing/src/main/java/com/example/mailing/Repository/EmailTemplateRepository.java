package com.example.mailing.Repository;

import com.example.mailing.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    // Trouver un template par son nom
    Optional<EmailTemplate> findByName(String name);
}
