package io.github.dudupuci.appdespesas.infrastructure.controllers.webhooks.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasPaymentWebhookResponseDto(
        String id,
        String event,
        String dateCreated,
        Account account,
        Payment payment
) {

    public record Account(
            String id,
            String ownerId
    ) {
    }

    public record Payment(
            String object,
            String id,
            String dateCreated,
            String customer,
            String checkoutSession,
            String paymentLink,
            BigDecimal value,
            BigDecimal netValue,
            BigDecimal originalValue,
            BigDecimal interestValue,
            String description,
            String billingType,
            String confirmedDate,
            String pixTransaction,
            String pixQrCodeId,
            String status,
            String dueDate,
            String originalDueDate,
            String paymentDate,
            String clientPaymentDate,
            Integer installmentNumber,
            String invoiceUrl,
            String invoiceNumber,
            String externalReference,
            boolean deleted,
            boolean anticipated,
            boolean anticipable,
            String creditDate,
            String estimatedCreditDate,
            String transactionReceiptUrl,
            String nossoNumero,
            String bankSlipUrl,
            String lastInvoiceViewedDate,
            String lastBankSlipViewedDate,
            Discount discount,
            Fine fine,
            Interest interest,
            boolean postalService,
            Object escrow,
            Object refunds
    ) {
    }

    public record Discount(
            BigDecimal value,
            String limitDate,
            Integer dueDateLimitDays,
            String type
    ) {
    }

    public record Fine(
            BigDecimal value,
            String type
    ) {
    }

    public record Interest(
            BigDecimal value,
            String type
    ) {
    }
}