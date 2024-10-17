package com.biwta.pontoon;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@OpenAPIDefinition
public class AuthorizationServerApplication {
    private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationServerApplication.class);

    public AuthorizationServerApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

    @PostConstruct
    public void generateDirectory() {
        File userImageFileDir = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/userImage");
        if (Files.notExists(userImageFileDir.toPath())) {
            try {
                Files.createDirectory(userImageFileDir.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + userImageFileDir.getPath());
                logger.error("Caught: " + ioException);
            }
        }
        File logFileDir = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/pontoonLog");
        if (Files.notExists(logFileDir.toPath())) {
            try {
                Files.createDirectory(logFileDir.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + logFileDir.getPath());
                logger.error("Caught: " + ioException);
            }
        }
        File employeeImageDir = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/employeeImage");
        if (Files.notExists(employeeImageDir.toPath())) {
            try {
                Files.createDirectory(employeeImageDir.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + employeeImageDir.getPath());
                logger.error("Caught: " + ioException);
            }
        }
        File employeeSignatureDirectory = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/employeeSignatureImage");
        if (Files.notExists(employeeSignatureDirectory.toPath())) {
            try {
                Files.createDirectory(employeeSignatureDirectory.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + employeeSignatureDirectory.getPath());
                logger.error("Caught: " + ioException);
            }
        }

        File pontoonImageDirectory = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/pontoonImage");
        if (Files.notExists(pontoonImageDirectory.toPath())) {
            try {
                Files.createDirectory(pontoonImageDirectory.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + pontoonImageDirectory.getPath());
                logger.error("Caught: " + ioException);
            }
        }

        File pontoonStatusDirectory = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/pontoonStatusDocument");
        if (Files.notExists(pontoonStatusDirectory.toPath())) {
            try {
                Files.createDirectory(pontoonStatusDirectory.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + pontoonStatusDirectory.getPath());
                logger.error("Caught: " + ioException);
            }
        }

        File pontoonMaintenanceDirectory = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/pontoonMaintenanceDocument");
        if (Files.notExists(pontoonMaintenanceDirectory.toPath())) {
            try {
                Files.createDirectory(pontoonMaintenanceDirectory.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + pontoonMaintenanceDirectory.getPath());
                logger.error("Caught: " + ioException);
            }
        }
        File pontoonTransferDocuments = new File(Objects.requireNonNull(environment.getProperty("fileStore.directory")) + "/pontoonTransferDocuments");
        if (Files.notExists(pontoonTransferDocuments.toPath())) {
            try {
                Files.createDirectory(pontoonTransferDocuments.toPath());
            } catch (IOException ioException) {
                logger.error("Can't create directory on: " + pontoonTransferDocuments.getPath());
                logger.error("Caught: " + ioException);
            }
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("tsuffixt@gmail.com");
        mailSender.setPassword("puzu znzi zoqv tmtx");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
