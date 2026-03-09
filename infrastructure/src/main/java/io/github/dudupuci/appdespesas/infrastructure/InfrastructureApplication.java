package io.github.dudupuci.appdespesas.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
    "io.github.dudupuci.appdespesas.domain",
    "io.github.dudupuci.appdespesas.application",
    "io.github.dudupuci.appdespesas.infrastructure"
})
@EnableTransactionManagement
@EnableFeignClients
public class InfrastructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApplication.class, args);
    }

}
