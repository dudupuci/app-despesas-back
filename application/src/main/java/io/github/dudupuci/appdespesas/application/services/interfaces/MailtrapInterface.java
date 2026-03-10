package io.github.dudupuci.appdespesas.application.services.interfaces;

import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.MailtrapEmailRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.MailtrapEmailResponseDto;

public interface MailtrapPort {
    MailtrapEmailResponseDto sendEmail(String token, MailtrapEmailRequestDto requestDto);
}
