package io.github.dudupuci.appdespesas.domain.utils;

import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.base.Entidade;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeImutavelUuid;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.TipoPeriodo;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.application.services.webservices.enums.BillingType;

import java.util.Calendar;
import java.util.Date;

public final class AppDespesasUtils {
    public static final String ATIVO = "ATIVO";
    public static final String INATIVO = "INATIVO";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

    public static boolean isEntidadeNotNull(Entidade entidade) {
        return entidade != null && entidade.getId() != null;
    }

    public static boolean isEntidadeNotNull(EntidadeUuid entidade) {
        return entidade != null && entidade.getId() != null;
    }

    public static boolean isEntidadeNotNull(EntidadeImutavelUuid entidade) {
        return entidade != null && entidade.getId() != null;
    }

    public static boolean isCategoriaAtiva(Categoria categoria) {
        return isEntidadeNotNull(categoria) && ATIVO.equalsIgnoreCase(categoria.getStatus().getNome());
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

    public static Date converterDataFromStringAnoMesDia(String dataString) {
        String[] partes = dataString.split("-");
        int ano = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]) - 1; // Meses em Calendar começam do zero (Janeiro = 0)
        int dia = Integer.parseInt(partes[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes, dia, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Alias para converterDataFromStringDiaMesAno para manter compatibilidade
     */
    public static Date converterStringParaDate(String dataString) {
        return converterDataFromStringDiaMesAno(dataString);
    }

    /**
     * Calcula o período (dataInicio e dataFim) baseado no tipo de período e data de referência
     *
     * @param tipoPeriodo    Tipo do período (DIA, SEMANA, MES)
     * @param dataReferencia Data de referência para o cálculo
     * @return Array com [dataInicio, dataFim]
     */
    public static Date[] calcularPeriodo(TipoPeriodo tipoPeriodo, Date dataReferencia) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataReferencia);

        Date dataInicio;
        Date dataFim;

        switch (tipoPeriodo) {
            case DIA:
                // Início do dia (00:00:00)
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dataInicio = calendar.getTime();

                // Fim do dia (23:59:59)
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                dataFim = calendar.getTime();
                break;

            case SEMANA:
                // Configurar para o primeiro dia da semana ser segunda-feira
                calendar.setFirstDayOfWeek(Calendar.MONDAY);

                // Obter o dia da semana atual (1=Domingo, 2=Segunda, ..., 7=Sábado)
                int diaDaSemanaAtual = calendar.get(Calendar.DAY_OF_WEEK);

                // Calcular quantos dias voltar para chegar à segunda-feira
                int diasParaVoltar;
                if (diaDaSemanaAtual == Calendar.SUNDAY) {
                    // Se for domingo, voltar 6 dias para chegar à segunda-feira
                    diasParaVoltar = 6;
                } else {
                    // Caso contrário, voltar (diaDaSemana - Calendar.MONDAY) dias
                    diasParaVoltar = diaDaSemanaAtual - Calendar.MONDAY;
                }

                // Início da semana (segunda-feira 00:00:00)
                calendar.add(Calendar.DAY_OF_MONTH, -diasParaVoltar);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dataInicio = calendar.getTime();

                // Fim da semana (domingo 23:59:59) - adicionar 6 dias à segunda-feira
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                dataFim = calendar.getTime();
                break;

            case MES:
                // Primeiro dia do mês (00:00:00)
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dataInicio = calendar.getTime();

                // Último dia do mês (23:59:59)
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                dataFim = calendar.getTime();
                break;

            default:
                throw new IllegalArgumentException("Tipo de período inválido: " + tipoPeriodo);
        }

        return new Date[]{dataInicio, dataFim};
    }

    public static String validaString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return str.trim();
    }

    public static String calculaDataVencimentoPorFormaPagamentoAndTipoCobranca(
            BillingType tipoPagamento,
            TipoRecursoPago tipoRecursoPago
    ) {

        if (tipoPagamento == null || tipoRecursoPago == null) {
            throw new IllegalArgumentException("Tipo de pagamento/Tipo de recurso pago não podem ser nulos");
        }

        long agora = System.currentTimeMillis();
        long dataVencimentoEmMilissegundos;

        // Retorna a data de vencimento baseada no tipo de recurso e forma de pagamento
        if (tipoRecursoPago == TipoRecursoPago.ASSINATURA) {
            dataVencimentoEmMilissegundos = switch (tipoPagamento) {
                case PIX -> agora + (15L * 60 * 1000); // 15 minutes
                case BOLETO -> agora + (3L * 24 * 60 * 60 * 1000); // 3 days
                default -> throw new IllegalArgumentException(
                        "Tipo de pagamento não suportado: " + tipoPagamento
                );
            };

        } else {
            throw new IllegalArgumentException(
                    "Recurso pago não encontrado: " + tipoRecursoPago
            );
        }

        Date dataVencimento = new Date(dataVencimentoEmMilissegundos);
        return String.format("%tF", dataVencimento);
    }


}
