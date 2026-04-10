package edu.eci.dosw.tdd.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile({"relational", "test"})
@EnableJpaRepositories(basePackages = "edu.eci.dosw.tdd.persistence.repository")
public class RelationalPersistenceConfig {
}