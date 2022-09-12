package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_professor")
public class Professor extends Funcionario {
    
}
