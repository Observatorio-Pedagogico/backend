package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.endereco;

import com.obervatorio_pedagogico.backend.domain.model.endereco.Endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}
