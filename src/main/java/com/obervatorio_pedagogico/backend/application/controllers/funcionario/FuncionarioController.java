package com.obervatorio_pedagogico.backend.application.controllers.funcionario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.request.FuncionarioRequest;
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

    private ResponseService responseService;

    private ModelMapperService modelMapperService;
    
    @GetMapping("/token")
    public ResponseEntity<Response<EnvelopeFuncionarioResponse>> getTodos(@RequestBody FuncionarioRequest funcionarioRequest){
        EnvelopeFuncionario envelopeFuncionario = funcionarioService.buscarFuncionarioPorToken(funcionarioRequest.getToken())
            .orElseThrow(() -> new NaoEncontradoException());

        return responseService.ok(modelMapperService.convert(envelopeFuncionario, EnvelopeFuncionarioResponse.class));
    }
}
