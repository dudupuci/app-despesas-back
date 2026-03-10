package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients.mailtrap;

import io.github.dudupuci.appdespesas.application.ports.services.MailtrapPort;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.MailtrapEmailRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.MailtrapEmailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailtrapPortAdapter implements MailtrapPort {

    private final MailtrapFeignClientAdapter feignClient;

    @Override
    public MailtrapEmailResponseDto sendEmail(String token, MailtrapEmailRequestDto requestDto) {
        return feignClient.sendEmail(token, requestDto);
    }
}
