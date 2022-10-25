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
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.dashboard.DashboardRepository;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina.DisciplinaRepository;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DashboardService {
    private final DisciplinaRepository disciplinaRepository;
    private final DashboardRepository dashboardRepository;

    public Dashboard gerarDashboardNotas(Predicate predicate, Boolean ignorarReprovadosPorFalta) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        gerarLegendarPorDisciplina(disciplinas, legendaPeriodoLetivos);

        for (String periodoLetivo : legendaPeriodoLetivos) {
            if (ignorarReprovadosPorFalta) {
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodoIgnorandoReprovadoPorFalta("NOTA", 1, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 1",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodoIgnorandoReprovadoPorFalta("NOTA", 2, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 2",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodoIgnorandoReprovadoPorFalta("NOTA", 3, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 3",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoPeriodoIgnorandoReprovadoPorFalta("MEDIA", periodoLetivo),
                    periodoLetivo, "MÉDIA",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoPeriodoIgnorandoReprovadoPorFalta("FINAL", periodoLetivo),
                    periodoLetivo, "FINAL",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
            } else {
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodo("NOTA", 1, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 1",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodo("NOTA", 2, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 2",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoOrdemPeriodo("NOTA", 3, periodoLetivo),
                    periodoLetivo, "AVALIAÇÃO 3",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoPeriodo("MEDIA", periodoLetivo),
                    periodoLetivo, "MÉDIA",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
                criarConjuntoDadosCalculandoMediaDoValor(
                    dashboardRepository.obterMediaDeNotaPorTipoPeriodo("FINAL", periodoLetivo),
                    periodoLetivo, "FINAL",
                    mapConjuntoDados,
                    legendaPeriodoLetivos
                );
            }
        }

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));
        return dashboard;
    }

    public Dashboard gerarDashboardFrequenciaENotas(Predicate predicate, Boolean ignorarReprovadosPorFalta) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        gerarLegendarPorDisciplina(disciplinas, legendaPeriodoLetivos);

        for (String periodoLetivo : legendaPeriodoLetivos) {
            if (ignorarReprovadosPorFalta) {
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMediaDeFrequenciaSituacaoPorPeriodo(periodoLetivo), periodoLetivo, "FREQUÊNCIA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMenorNotaPorPeriodoIgnorandoReprovadoPorFalta(periodoLetivo), periodoLetivo, "MENOR NOTA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMaiorNotaPorPeriodoIgnorandoReprovadoPorFalta(periodoLetivo), periodoLetivo, "MAIOR NOTA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMediaDeNotaPorTipoPeriodoIgnorandoReprovadoPorFalta("MEDIA", periodoLetivo), periodoLetivo, "MÉDIA", mapConjuntoDados, legendaPeriodoLetivos);
            } else {
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMediaDeFrequenciaSituacaoPorPeriodo(periodoLetivo), periodoLetivo, "FREQUÊNCIA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMenorNotaPorPeriodo(periodoLetivo), periodoLetivo, "MENOR NOTA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMaiorNotaPorPeriodo(periodoLetivo), periodoLetivo, "MAIOR NOTA", mapConjuntoDados, legendaPeriodoLetivos);
                criarConjuntoDadosCalculandoMediaDoValor(dashboardRepository.obterMediaDeNotaPorTipoPeriodo("MEDIA", periodoLetivo), periodoLetivo, "MÉDIA", mapConjuntoDados, legendaPeriodoLetivos);
            }
        }

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));
        return dashboard;
    }

    public void gerarLegendarPorDisciplina(Iterable<Disciplina> disciplinas, Set<String> legendas) {
        for (Disciplina disciplina : disciplinas) {
            legendas.add(disciplina.getPeriodoLetivo());
        }
    }

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

    private void criarConjuntoDadosCalculandoMediaDoValor(Float valor, String periodo, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(valor);

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            conjuntoDados.getDados().add(valor);
        }
    }

    private void criarConjuntoDadosSiatuacaoAluno(Disciplina disciplina, SituacaoDisciplina situacaoDisciplina, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(disciplina.getPeriodoLetivo());
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(situacaoDisciplina.name());
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(situacaoDisciplina.name());
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSiatuacao(situacaoDisciplina).floatValue());

            mapConjuntoDados.put(situacaoDisciplina.name(), conjuntoDados);
        } else {
            List<Float> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunosPorSiatuacao(situacaoDisciplina);
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos.floatValue());
            } else {
                Float valor = conjuto.get(index);
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
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunosPorSexo(sexo).floatValue());

            mapConjuntoDados.put(sexo.name(), conjuntoDados);
        } else {
            List<Float> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunosPorSexo(sexo);
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos.floatValue());
            } else {
                Float valor = conjuto.get(index);
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
            conjuntoDados.getDados().add(disciplina.getQuantidadeAlunos().floatValue());

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            List<Float> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = disciplina.getQuantidadeAlunos();
            int index = getIndexFromSet(legendaPeriodoLetivos, disciplina.getPeriodoLetivo());
            if (index > conjuto.size() - 1) {
                conjuto.add(quantidadeAlunos.floatValue());
            } else {
                Float valor = conjuto.get(index);
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
