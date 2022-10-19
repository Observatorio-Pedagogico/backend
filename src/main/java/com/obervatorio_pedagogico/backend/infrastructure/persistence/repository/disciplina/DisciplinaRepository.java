package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>, QuerydslPredicateExecutor<Disciplina>, JpaSpecificationExecutor<Disciplina> {

    public Optional<Disciplina> findDisciplinaByCodigoAndPeriodoLetivo(String codigo, String periodoLetivo);
}
