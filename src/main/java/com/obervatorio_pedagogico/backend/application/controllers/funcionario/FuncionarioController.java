package com.obervatorio_pedagogico.backend.application.controllers.funcionario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.application.services.usuario.ProfessorService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.request.UpdateProfessorDisciplinaRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.EnvelopeFuncionarioResponse;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/funcionario")
@AllArgsConstructor
@CrossOrigin
public class FuncionarioController {

    private FuncionarioService funcionarioService;
    private ProfessorService professorService;
    private ResponseService responseService;
    private ModelMapperService modelMapperService;
    
    @GetMapping("/token")
    public ResponseEntity<Response<EnvelopeFuncionarioResponse>> getTodos(@RequestHeader("token") String token) {
        EnvelopeFuncionario envelopeFuncionario = funcionarioService.buscarFuncionarioPorToken(token)
            .orElseThrow(() -> new NaoEncontradoException());

        return responseService.ok(modelMapperService.convert(envelopeFuncionario, EnvelopeFuncionarioResponse.class));
    }

    @PostMapping("/professor/adicionar-disciplina")
    public void adicionarDisciplinaAoProfessor(@RequestBody UpdateProfessorDisciplinaRequest professorDisciplinaRequest) {
        professorService.adicionarDisciplina(professorDisciplinaRequest.getIdProfessor(), professorDisciplinaRequest.getCodigos());
    }

    @PostMapping("/professor/remover-disciplina")
    public void removerDisciplinaDoProfessor(@RequestBody UpdateProfessorDisciplinaRequest professorDisciplinaRequest) {
        professorService.removerDisciplina(professorDisciplinaRequest.getIdProfessor(), professorDisciplinaRequest.getCodigos());
    }
}
