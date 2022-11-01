package com.obervatorio_pedagogico.backend.application.services.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
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

    public Dashboard gerarDashboardNotas(Predicate predicate, Boolean ignorarAusencia) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        gerarLegendarPorDisciplina(disciplinas, legendaPeriodoLetivos);

        for (String periodoLetivo : legendaPeriodoLetivos) {
            if (ignorarAusencia) {
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

        if (dashboard.getConjuntoDados().isEmpty()) {
            throw new NaoEncontradoException();
        }

        return dashboard;
    }

    public Dashboard gerarDashboardFrequenciaENotas(Predicate predicate, Boolean ignorarAusencia) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        gerarLegendarPorDisciplina(disciplinas, legendaPeriodoLetivos);

        for (String periodoLetivo : legendaPeriodoLetivos) {
            if (ignorarAusencia) {
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

        if (dashboard.getConjuntoDados().isEmpty()) {
            throw new NaoEncontradoException();
        }

        return dashboard;
    }

    public Dashboard gerarDashboardSexo(Predicate predicate, Boolean ignorarAusencia) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        disciplinas.forEach(disciplina -> {
            for (Sexo sexo : Sexo.values()) {
                if (ignorarAusencia) {
                    criarConjuntoDadosSexo(disciplina.getQuantidadeAlunosPorSexo(sexo, ignorarAusencia).floatValue(), disciplina.getPeriodoLetivo(), sexo.name(), mapConjuntoDados, legendaPeriodoLetivos);
                } else {
                    criarConjuntoDadosSexo(disciplina.getQuantidadeAlunosPorSexo(sexo).floatValue(), disciplina.getPeriodoLetivo(), sexo.name(), mapConjuntoDados, legendaPeriodoLetivos);
                }
            }
            if (ignorarAusencia ) {
                criarConjuntoTotalDados(disciplina.getQuantidadeAlunos(ignorarAusencia).floatValue(), disciplina.getPeriodoLetivo(), "TOTAL", mapConjuntoDados, legendaPeriodoLetivos);
            } else {
                criarConjuntoTotalDados(disciplina.getQuantidadeAlunos().floatValue(), disciplina.getPeriodoLetivo(), "TOTAL", mapConjuntoDados, legendaPeriodoLetivos);
            }
        });

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));

        if (dashboard.getConjuntoDados().isEmpty()) {
            throw new NaoEncontradoException();
        }

        return dashboard;
    }

    public Dashboard gerarDashboardSituacaoAlunos(Predicate predicate, Boolean ignorarAusencia) {
        Map<String, ConjuntoDados> mapConjuntoDados = new LinkedHashMap<>();
        Set<String> legendaPeriodoLetivos = new LinkedHashSet<>();

        Dashboard dashboard = new Dashboard();
        Iterable<Disciplina> disciplinas = this.disciplinaRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "periodoLetivo"));

        disciplinas.forEach(disciplina -> {
            for (SituacaoDisciplina situacaoDisciplina : SituacaoDisciplina.values()) {
                if (!ignorarAusencia || (ignorarAusencia && !situacaoDisciplina.isAusencia())) {
                    criarConjuntoDadosSiatuacaoAluno(disciplina.getQuantidadeAlunosPorSiatuacao(situacaoDisciplina).floatValue(), disciplina.getPeriodoLetivo(), situacaoDisciplina.name(), mapConjuntoDados, legendaPeriodoLetivos);
                }
            }
            if (ignorarAusencia ) {
                criarConjuntoTotalDados(disciplina.getQuantidadeAlunos(ignorarAusencia).floatValue(), disciplina.getPeriodoLetivo(), "MATRICULADOS", mapConjuntoDados, legendaPeriodoLetivos);
            } else {
                criarConjuntoTotalDados(disciplina.getQuantidadeAlunos().floatValue(), disciplina.getPeriodoLetivo(), "MATRICULADOS", mapConjuntoDados, legendaPeriodoLetivos);
            }
        });

        dashboard.setLegendas(new ArrayList<>(legendaPeriodoLetivos));
        dashboard.setConjuntoDados(new ArrayList<>(mapConjuntoDados.values()));

        if (dashboard.getConjuntoDados().isEmpty()) {
            throw new NaoEncontradoException();
        }

        return dashboard;
    }

    private void criarConjuntoDadosCalculandoMediaDoValor(Float valor, String periodo, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            conjuntoDados.getDados().add(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
        }
    }

    private void criarConjuntoDadosSiatuacaoAluno(Float valor, String periodo, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(periodo);
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            List<BigDecimal> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = valor.intValue();
            int index = getIndexFromSet(legendaPeriodoLetivos, periodo);
            if (index > conjuto.size() - 1) {
                conjuto.add(new BigDecimal(quantidadeAlunos.floatValue()).setScale(2, RoundingMode.HALF_UP));
            } else {
                BigDecimal valorIndex = conjuto.get(index);
                valorIndex = valorIndex.add(new BigDecimal(quantidadeAlunos));
                conjuto.set(index, valorIndex);
            }
        }
    }

    private void criarConjuntoDadosSexo(Float valor, String periodo, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(periodo);
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            List<BigDecimal> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = valor.intValue();
            int index = getIndexFromSet(legendaPeriodoLetivos, periodo);
            if (index > conjuto.size() - 1) {
                conjuto.add(new BigDecimal(quantidadeAlunos.floatValue()).setScale(2, RoundingMode.HALF_UP));
            } else {
                BigDecimal valorIndex = conjuto.get(index);
                valorIndex = valorIndex.add(new BigDecimal(quantidadeAlunos));
                conjuto.set(index, valorIndex);
            }
        }
    }

    private void criarConjuntoTotalDados(Float valor, String periodo, String label, Map<String, ConjuntoDados> mapConjuntoDados, Set<String> legendaPeriodoLetivos) {
        legendaPeriodoLetivos.add(periodo);
        ConjuntoDados conjuntoDados = mapConjuntoDados.get(label);
        if (Objects.isNull(conjuntoDados)) {
            conjuntoDados = new ConjuntoDados();
            conjuntoDados.setLegenda(label);
            conjuntoDados.getDados().add(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));

            mapConjuntoDados.put(label, conjuntoDados);
        } else {
            List<BigDecimal> conjuto = conjuntoDados.getDados();
            Integer quantidadeAlunos = valor.intValue();
            int index = getIndexFromSet(legendaPeriodoLetivos, periodo);
            if (index > conjuto.size() - 1) {
                conjuto.add(new BigDecimal(quantidadeAlunos.floatValue()).setScale(2, RoundingMode.HALF_UP));
            } else {
                BigDecimal valorIndex = conjuto.get(index);
                valorIndex = valorIndex.add(new BigDecimal(quantidadeAlunos));
                conjuto.set(index, valorIndex);
            }
        }
    }

    private void gerarLegendarPorDisciplina(Iterable<Disciplina> disciplinas, Set<String> legendas) {
        for (Disciplina disciplina : disciplinas) {
            legendas.add(disciplina.getPeriodoLetivo());
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
