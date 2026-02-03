package io.github.dudupuci.appdespesas.utils;

import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;

import java.util.Calendar;
import java.util.Date;

public final class AppDespesasUtils {

    public static boolean isEntidadeNotNull(Entidade entidade) {
        return entidade != null && entidade.getId() != null;
    }

    public static boolean isCategoriaAtiva(Categoria categoria) {
        return isEntidadeNotNull(categoria) && AppDespesasConstants.ATIVO.equalsIgnoreCase(categoria.getStatus().getNome());
    }

    public static Date converterDataFromStringDiaMesAno(String dataString) {
        String[] partes = dataString.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]) - 1; // Meses em Calendar começam do zero (Janeiro = 0)
        int ano = Integer.parseInt(partes[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
