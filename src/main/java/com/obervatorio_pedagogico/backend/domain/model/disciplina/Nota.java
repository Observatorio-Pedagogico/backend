package com.obervatorio_pedagogico.backend.domain.model.disciplina;

import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    
    @Column(name = "valor")
    private BigDecimal valor;
    
    @Column(name = "ordem")
    private Integer ordem;
    
    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    public enum Tipo {
        NOTA, MEDIA, FINAL;

        public Boolean isNota() {
            return NOTA.equals(this);
        }

        public Boolean isMedia() {
            return MEDIA.equals(this);
        }

        public Boolean isFinal() {
            return FINAL.equals(this);
        }
    }
}
