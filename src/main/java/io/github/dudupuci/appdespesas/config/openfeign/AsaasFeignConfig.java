package io.github.dudupuci.appdespesas.config.openfeign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Feign para a integração com a API do Asaas.
 * Adiciona o header "access_token" com a chave de API para autenticação em todas as requisições feitas pelo Feign.
 * A chave de API é lida do arquivo de propriedades usando a anotação @Value.
 */
public class AsaasFeignConfig {

    @Value("${integrations.asaas.sandbox.apiKey}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("access_token", apiKey);
        };
    }
}
