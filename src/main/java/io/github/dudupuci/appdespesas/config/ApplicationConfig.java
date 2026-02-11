package io.github.dudupuci.appdespesas.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Getter
public class ApplicationConfig {

    @Value("${tudin.app.super-adm-id}")
    private String superAdmIdString;

    @Value("${tudin.app.super-adm-name}")
    private String superAdminName;

    @Value("${tudin.app.super-adm-middleName}")
    private String superAdminMiddleName;

    @Value("${tudin.app.super-adm-email}")
    private String superAdminEmail;

    @Value("${tudin.app.super-adm-username}")
    private String superAdminUsername;

    @Value("${tudin.app.super-adm-password}")
    private String superAdminPassword;

    public UUID getSuperAdmId() {
        return UUID.fromString(superAdmIdString);
    }
}

