package org.shareio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"org.shareio.backend.infrastructure.controller",
        "org.shareio.backend.infrastructure.dbadapter",
        "org.shareio.backend.core.usecases.service",
        "org.shareio.backend.security",
        "org.shareio.backend.infrastructure.email",
})
@EnableJpaRepositories("org.shareio.backend.infrastructure.dbadapter")
@EntityScan("org.shareio.backend.infrastructure.dbadapter")
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.run(args);
    }
}
