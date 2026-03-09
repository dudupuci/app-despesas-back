package io.github.dudupuci.appdespesas.application.services.webservices.dtos.request;

import java.util.List;

public record MailtrapEmailRequestDto(
        Address from,
        List<Address> to,
        String subject,
        String html
) {

    public record Address(
            String email,
            String name
    ) {
    }

}