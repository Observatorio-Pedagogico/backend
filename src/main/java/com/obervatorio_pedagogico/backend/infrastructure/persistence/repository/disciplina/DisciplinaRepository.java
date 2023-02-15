package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>, QuerydslPredicateExecutor<Disciplina>, JpaSpecificationExecutor<Disciplina> {

    public Optional<Disciplina> findDisciplinaByCodigoAndPeriodoLetivo(String codigo, String periodoLetivo);

    public List<Disciplina> findDisciplinaByCodigo(String codigo);

    @Query(value = "select * from t_disciplina disciplina where disciplina.codigo in :codigos", nativeQuery = true)
    public List<Disciplina> findDisciplinaByCodigo(@Param("codigos") List<String> codigos);

    @Query(value = "select distinct on (disciplina.codigo) disciplina.* from t_disciplina disciplina", nativeQuery = true)
    public List<Disciplina> findDisciplinaIgnorePeriodo();
}
