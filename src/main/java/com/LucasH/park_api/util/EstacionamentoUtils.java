package com.LucasH.park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
