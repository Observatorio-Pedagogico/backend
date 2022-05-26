package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.AlunoModel;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {
    
}
