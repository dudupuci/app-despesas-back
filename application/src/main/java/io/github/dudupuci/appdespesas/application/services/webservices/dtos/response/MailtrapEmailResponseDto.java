package io.github.dudupuci.appdespesas.application.services.webservices.dtos.response;

import java.util.List;

public record MailtrapEmailResponseDto(
        Boolean success,
        List<String> message_ids
) {
}
