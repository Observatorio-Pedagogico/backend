package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;

@Repository
public interface DashboardRepository extends JpaRepository<Disciplina, Long> {
    
    @Query(value = "select sum(nota.valor) / count(1) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "where nota.tipo = 'MEDIA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMediaDeNotaTipoMediaPorPeriodo(@Param("periodo") String periodo);

    @Query(value = "select sum(nota.valor) / count(1) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "join t_frequencia_situacao frequencia_situacao on frequencia_situacao.id_disciplina = disciplina.id " +
    "where nota.tipo = 'MEDIA' and not frequencia_situacao.situacao_disciplina = 'REPROVADO_POR_FALTA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMediaDeNotaTipoMediaPorPeriodoIgnorandoReprovadoPorFalta(@Param("periodo") String periodo);
    
    @Query(value = "select sum(frequencia_situacao.frequencia) / count(1) from t_frequencia_situacao frequencia_situacao " +
    "join t_disciplina disciplina on disciplina.id = frequencia_situacao.id_disciplina " +
    "where disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMediaDeFrequenciaSituacaoPorPeriodo(@Param("periodo") String periodo);
    
    @Query(value = "select min(nota.valor) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "where nota.tipo = 'NOTA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMenorNotaPorPeriodo(@Param("periodo") String periodo);

    @Query(value = "select min(nota.valor) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "join t_frequencia_situacao frequencia_situacao on frequencia_situacao.id_disciplina = disciplina.id " +
    "where not frequencia_situacao.situacao_disciplina = 'REPROVADO_POR_FALTA' and nota.tipo = 'NOTA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMenorNotaPorPeriodoIgnorandoReprovadoPorFalta(@Param("periodo") String periodo);

    @Query(value = "select max(nota.valor) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "where nota.tipo = 'NOTA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMaiorNotaPorPeriodo(@Param("periodo") String periodo);

    @Query(value = "select max(nota.valor) from t_nota nota " +
    "join t_disciplina disciplina on disciplina.id = nota.id_disciplina " +
    "join t_frequencia_situacao frequencia_situacao on frequencia_situacao.id_disciplina = disciplina.id " +
    "where not frequencia_situacao.situacao_disciplina = 'REPROVADO_POR_FALTA' and nota.tipo = 'NOTA' and disciplina.periodo_letivo = :periodo", nativeQuery = true)
    public Float obterMaiorNotaPorPeriodoIgnorandoReprovadoPorFalta(@Param("periodo") String periodo);
}
