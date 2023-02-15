package com.obervatorio_pedagogico.backend.presentation.dto.usuario.response;

import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.response.DisciplinaResumidoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorResponse extends UsuarioResponse {

    private List<DisciplinaResumidoResponse> disciplinas;
}
