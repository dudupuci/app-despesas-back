package io.github.dudupuci.appdespesas.config.app;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessagesConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages/pt_BR_messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setCacheSeconds(3600);
        return ms;
    }
}
