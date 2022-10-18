package com.obervatorio_pedagogico.backend.domain.model.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Dashboard {
    private List<String> legendas;

    private List<ConjuntoDados> conjuntoDados;
}
