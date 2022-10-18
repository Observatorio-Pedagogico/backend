package com.obervatorio_pedagogico.backend.application.services.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.dashboard.ConjuntoDados;
import com.obervatorio_pedagogico.backend.domain.model.dashboard.Dashboard;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DashboardServices {
    private final DisciplinaRepository disciplinaRepository;

    public void gerarDashboardSexo() {
        Map<String, ConjuntoDados> mapConjuntoDados = new HashMap<>();
        Set<String> legendaPeriodoLetivos = new HashSet<>();

        Dashboard dashboard = new Dashboard();
        List<Disciplina> disciplinas = this.disciplinaRepository.findAll();

        disciplinas.stream().forEach(disciplina -> {
            criarConjuntoDadosSexo(disciplina, Sexo.MASCULINO, mapConjuntoDados, legendaPeriodoLetivos);
            criarConjuntoDadosSexo(disciplina, Sexo.FEMININO, mapConjuntoDados, legendaPeriodoLetivos);
        });

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados((List<ConjuntoDados>) mapConjuntoDados.values());
    }

    private void criarConjuntoDadosSexo(Disciplina disciplina, Sexo sexo, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(criarNomeKeyMap(disciplina.getPeriodoLetivo(), sexo.name()));
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(sexo.name());
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSexo(sexo));

            mapConjuntoDados.put(disciplina.getPeriodoLetivo(), conjuntoDados);
        } else {
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSexo(sexo));
        }
    }

    private String criarNomeKeyMap(String periodoLetivo, String diferencial) {
        return periodoLetivo.concat("-").concat(diferencial);
    }
}
