package io.github.dudupuci.appdespesas.controllers.dtos.request.waha;

public record WahaWebhookDto(
        String event,
        String session,
        Payload payload
) {
    public record Payload(
            String id,
            Long timestamp,
            String from,
            String to,
            String body,
            Boolean fromMe,
            Boolean hasMedia,
            Media media
    ) {}

    public record Media(
            String url,
            String mimetype,
            String filename
    ) {}
}