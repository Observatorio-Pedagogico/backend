package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>, QuerydslPredicateExecutor<Aluno>, JpaSpecificationExecutor<Aluno> {
    public Optional<Aluno> findAlunoByMatricula(String matricula);
}
