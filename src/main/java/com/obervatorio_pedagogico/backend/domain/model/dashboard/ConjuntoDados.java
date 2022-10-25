package com.obervatorio_pedagogico.backend.domain.model.dashboard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConjuntoDados {
    private String legenda;
    private List<BigDecimal> dados;

    public ConjuntoDados(String legenda, List<BigDecimal> dados) {
        this.legenda = legenda;
        this.dados = dados;
    }

    public ConjuntoDados() {
        this.initCollections();
    }

    private void initCollections() {
        this.dados = new ArrayList<>();
    }
    
}
