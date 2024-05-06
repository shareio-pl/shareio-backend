package org.shareio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"org.shareio.backend.infrastructure.controller",
        "org.shareio.backend.infrastructure.dbadapter",
        "org.shareio.backend.core.usecases.service",
})
@EnableJpaRepositories("org.shareio.backend.infrastructure.dbadapter")
@EntityScan("org.shareio.backend.infrastructure.dbadapter")
public class Main {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        // ... customize application settings here
        application.run(args);
    }
}
