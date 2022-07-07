package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.disciplina;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    public Optional<Disciplina> findDisciplinaByCodigoAndPeriodoLetivo(String codigo, String periodoLetivo);
}
