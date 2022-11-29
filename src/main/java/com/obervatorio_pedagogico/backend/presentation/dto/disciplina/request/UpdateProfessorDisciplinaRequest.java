package com.obervatorio_pedagogico.backend.presentation.dto.disciplina.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProfessorDisciplinaRequest {
    private Long idProfessor;
    private List<String> codigos;
}
