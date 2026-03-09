package io.github.dudupuci.appdespesas.domain.converters;

import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class CanalNotificacaoConverter implements AttributeConverter<Set<CanalNotificacao>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(Set<CanalNotificacao> canais) {
        if (canais == null || canais.isEmpty()) {
            return null;
        }
        return canais.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public Set<CanalNotificacao> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(CanalNotificacao::valueOf)
                .collect(Collectors.toSet());
    }
}

