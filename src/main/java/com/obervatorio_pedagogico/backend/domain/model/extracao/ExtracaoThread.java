package com.obervatorio_pedagogico.backend.domain.model.extracao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtracaoThread {
    
    private Integer porcentagemDeEnvio = 0;

    private Integer totalLinhas = 0;

    private Integer linhaAtual = 1;

    private Extracao extracao;

    public void calcularPorcentagem() {
        if (porcentagemDeEnvio < 100) {
            porcentagemDeEnvio = (linhaAtual * 100) / totalLinhas;
        }
    }

    public void setRunnable(Runnable runnable) {
        new Thread(runnable).start();
    }
}