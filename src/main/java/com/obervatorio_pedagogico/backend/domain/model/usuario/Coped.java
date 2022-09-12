package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_funcionario_coped")
public class Coped extends Funcionario {
    
    @Column(name = "cargo")
    @Enumerated(EnumType.STRING)
    private TipoCargo cargo;

    public enum TipoCargo {
        COORDENADORA;

        public boolean isCoordenadora(){
            return COORDENADORA.equals(this);
        }
    }
}
