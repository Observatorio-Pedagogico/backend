package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao;

import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoModel;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ExtracaoRepository extends JpaRepository<ExtracaoModel, Long> {
    
}
