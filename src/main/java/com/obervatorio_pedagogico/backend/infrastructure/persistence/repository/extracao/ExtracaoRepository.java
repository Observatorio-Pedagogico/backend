package com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao;

import org.springframework.stereotype.Repository;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ExtracaoRepository extends JpaRepository<Extracao, Long> {
    
    @Query(value = "SELECT e FROM Extracao e WHERE e.status IN :status")
    public List<Extracao> findByStatus(@Param("status") Status status);
    
    @Query(value = "SELECT e FROM Extracao e WHERE :periodoLetivo IN e.periodoLetivo")
    public List<Extracao> findByPeriodoLetivo(@Param("periodoLetivo") String periodoLetivo);
}
