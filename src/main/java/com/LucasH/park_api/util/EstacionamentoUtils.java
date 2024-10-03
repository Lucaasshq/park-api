package com.LucasH.park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    //antes do substring - 2024-09-29T14:58:27.473920
    //com substring - 2024-09-29T14:58:27
    // com substring e replace 20240929-145827

    public static String gerarRecibo() {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0, 19);

        return recibo.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
        // Método replace() ultilizando para remover o caracter ou remover e substituir por outro

    }

    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        final double PRIMEIROS_15_MINUTOS = 5.00;
        final double PRIMEIROS_60_MINUTOS = 9.25;
        final double ADICIONAL_15_MINUTOS = 1.75;

        long minutos = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutos <= 15) {
            total += PRIMEIROS_15_MINUTOS;
        } else if (minutos <= 60) {
            total += PRIMEIROS_60_MINUTOS;
        } else {
            long tempoAdicional = minutos - 60;
            long intervalosAdicionais = (tempoAdicional + 14) / 15;
            total += PRIMEIROS_60_MINUTOS + (ADICIONAL_15_MINUTOS * intervalosAdicionais);
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {
        final double DESCONTO_PERCENTUAL = 0.30;

        BigDecimal desconto = null;

        if (numeroDeVezes % 10 == 0 && numeroDeVezes > 0) {
            desconto = custo.multiply(BigDecimal.valueOf(DESCONTO_PERCENTUAL));
        } else {
            desconto = new BigDecimal(0);
        }



        return desconto.setScale(2, RoundingMode.HALF_EVEN);


    }
}
