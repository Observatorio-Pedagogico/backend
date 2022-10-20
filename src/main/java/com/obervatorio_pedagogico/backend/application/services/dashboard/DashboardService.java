package com.obervatorio_pedagogico.backend.application.services.dashboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao.SituacaoDisciplina;
import com.obervatorio_pedagogico.backend.domain.model.dashboard.ConjuntoDados;
import com.obervatorio_pedagogico.backend.domain.model.dashboard.Dashboard;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DashboardService {
    private final DisciplinaRepository disciplinaRepository;

    public Dashboard gerarDashboardSexo(Predicate predicate) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        disciplinas.forEach(disciplina -> {
            for (Sexo sexo : Sexo.values()) {
                criarConjuntoDadosSexo(disciplina, sexo, mapConjuntoDados, legendaPeriodoLetivos);
            }
            criarConjuntoTotalDados(disciplina, "TOTAL", mapConjuntoDados, legendaPeriodoLetivos);
        });

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));

        return dashboard;
    }

    public Dashboard gerarDashboardSituacaoAlunos(Predicate predicate) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        disciplinas.forEach(disciplina -> {
            for (SituacaoDisciplina situacaoDisciplina : SituacaoDisciplina.values()) {
                criarConjuntoDadosSiatuacaoAluno(disciplina, situacaoDisciplina, mapConjuntoDados, legendaPeriodoLetivos);
            } 
            criarConjuntoTotalDados(disciplina, "MATRICULADOS", mapConjuntoDados, legendaPeriodoLetivos);
        });

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));

        return dashboard;
    }

    private void criarConjuntoDadosSiatuacaoAluno(Disciplina disciplina, SituacaoDisciplina situacaoDisciplina, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(situacaoDisciplina.name());
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(situacaoDisciplina.name());
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSiatuacao(situacaoDisciplina));

            mapConjuntoDados.put(situacaoDisciplina.name(), conjuntoDados);
        } else {
            List<Integer> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunosPorSiatuacao(situacaoDisciplina);
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos);
            } else {
                Integer valor = conjuto.get(index);
                valor += quantidadeAlunos;
                conjuto.set(index, valor);
            }
        }
    }

    private void criarConjuntoDadosSexo(Disciplina disciplina, Sexo sexo, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(sexo.name());
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(sexo.name());
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSexo(sexo));

            mapConjuntoDados.put(sexo.name(), conjuntoDados);
        } else {
            List<Integer> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunosPorSexo(sexo);
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos);
            } else {
                Integer valor = conjuto.get(index);
                valor += quantidadeAlunos;
                conjuto.set(index, valor);
            }
        }
    }

    private void criarConjuntoTotalDados(Disciplina disciplina, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunos());

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            List<Integer> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunos();
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos);
            } else {
                Integer valor = conjuto.get(index);
                valor += quantidadeAlunos;
                conjuto.set(index, valor);
            }
        }
    }

    private <T> Integer getIndexFromSet(Set<T> set, T object) {
        int count = 0;
        for (T setElement : set) {
            if (setElement.equals(object)) {
                return count;
            }
            count++;
        }
        return -1;
    }
}
