package com.obervatorio_pedagogico.backend.application.controllers.funcionario;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.FuncionarioResponse;
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

    private ResponseService responseService;

    private ModelMapperService modelMapperService;
    
    @GetMapping("/token")
    public ResponseEntity<Response<FuncionarioResponse>> getTodos(@RequestHeader("token") String token) {
        EnvelopeFuncionario envelopeFuncionario = funcionarioService.buscarFuncionarioPorToken(token)
            .orElseThrow(() -> new NaoEncontradoException());

        return responseService.ok(modelMapperService.convert(envelopeFuncionario.getFuncionario(), FuncionarioResponse.class));
    }
}
