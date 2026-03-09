package io.github.dudupuci.appdespesas.infrastructure.config.app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getValueAsString();
        try {
            return AppDespesasUtils.converterDataFromStringDiaMesAno(dateString);
        } catch (IllegalArgumentException e) {
            throw new IOException("Erro ao deserializar data: " + e.getMessage(), e);
        }
    }
}
