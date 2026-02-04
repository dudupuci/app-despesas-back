package io.github.dudupuci.appdespesas.controllers.dtos.request.waha;

public record WahaMessageRequestDto(
        String chatId,
        String text,
        String session
) {
}
