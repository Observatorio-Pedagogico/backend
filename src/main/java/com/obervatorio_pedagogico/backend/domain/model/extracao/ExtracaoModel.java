package com.obervatorio_pedagogico.backend.domain.model.extracao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_extracao")
public class ExtracaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private Status status;

    @Column(name = "ano_letivo")
    private Integer anoLetivo;
    
    @Column(name = "ultima_data_hora_atualizacao")
    private LocalDateTime ultimaDataHoraAtualizacao;

    public enum Status {
        ATIVA, CANCELADA
    }
}
