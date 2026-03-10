package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients.mailtrap;

import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.MailtrapEmailRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.MailtrapEmailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "mailtrapFeignClient",
        url = "${integrations.mailtrap.api.url}"
)
public interface MailtrapFeignClientAdapter {

    @PostMapping("/send/4436096")
    MailtrapEmailResponseDto sendEmail(
            @RequestHeader("Authorization") String token,
            @RequestBody MailtrapEmailRequestDto request
    );

}