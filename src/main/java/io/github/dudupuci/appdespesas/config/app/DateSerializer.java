package io.github.dudupuci.appdespesas.config.app;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat(AppDespesasUtils.DD_MM_YYYY);

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(formatter.format(date));
    }
}
