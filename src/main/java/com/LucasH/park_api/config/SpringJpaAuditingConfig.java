package com.LucasH.park_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class SpringJpaAuditingConfig implements AuditorAware<String> {
    // classe SpringJpaAuditingConfig que você forneceu está configurando
    // o JPA Auditing em uma aplicação Spring, que é uma funcionalidade que
    // permite rastrear automaticamente informações como "quem criou" e "quem
    // modificou" uma entidade em um banco de dados.
    @Override
    public Optional<String> getCurrentAuditor() {

        // esse método vai recupegar o nome da pessoa que esta autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Optional.of(authentication.getName());
            //Este método é responsável por retornar o auditor (a pessoa atualmente autenticada no sistema)
            // no formato Optional<String>.
        }

        return null;
    }
}
