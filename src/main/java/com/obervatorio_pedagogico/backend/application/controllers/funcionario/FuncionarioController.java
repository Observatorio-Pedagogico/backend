package com.obervatorio_pedagogico.backend.application.controllers.funcionario;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.application.services.usuario.ProfessorService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.request.UpdateProfessorDisciplinaRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.EnvelopeFuncionarioResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.ProfessorResponse;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/professor/{id}")
    public ResponseEntity<Response<ProfessorResponse>> getProfessor(@PathVariable("id") Long idProfessor) {
        Professor professor = professorService.buscarPorId(idProfessor);
        return responseService.ok(modelMapperService.convert(professor, ProfessorResponse.class));
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
