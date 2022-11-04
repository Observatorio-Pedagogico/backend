package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>, QuerydslPredicateExecutor<Aluno>, JpaSpecificationExecutor<Aluno> {
    public Optional<Aluno> findAlunoByMatricula(String matricula);

    @Query(value = "select * from t_aluno aluno " +
    "join t_disciplina_alunos disciplinas_alunos on disciplinas_alunos.id_aluno = aluno.id " +
    "join t_disciplina disciplina on disciplina.id = disciplinas_alunos.id_disciplina " +
    "where ((:codigos) is null or disciplina.codigo in (:codigos)) and ((:periodos) is null or disciplina.periodo_letivo in (:periodos))", nativeQuery = true)
    public Page<Aluno> findAlunoByParams(@Param("codigos") List<String> codigos,
                                         @Param("periodos") List<String> periodos,
                                         Pageable pageable);

    @Query(value = "select distinct aluno.* from t_aluno aluno " +
    "join t_disciplina_alunos disciplinas_alunos on disciplinas_alunos.id_aluno = aluno.id " +
    "join t_disciplina disciplina on disciplina.id = disciplinas_alunos.id_disciplina " +
    "join t_frequencia_situacao frequencia_situacao on frequencia_situacao.id_disciplina = disciplina.id " +
    "where ((:codigos) is null or disciplina.codigo in (:codigos)) and ((:periodos) is null or disciplina.periodo_letivo in (:periodos)) " +
    "and not frequencia_situacao.situacao_disciplina in ('REPROVADO_POR_FALTA', 'CANCELADO', 'TRANCADO')", nativeQuery = true)
    public Page<Aluno> findAlunoByParamsIgnorarAusencia(@Param("codigos") List<String> codigos,
                                         @Param("periodos") List<String> periodos,
                                         Pageable pageable);
}
