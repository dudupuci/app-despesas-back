package io.github.dudupuci.appdespesas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AppDespesasApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppDespesasApplication.class, args);
    }

}
