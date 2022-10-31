package com.obervatorio_pedagogico.backend.application.controllers.autenticacao;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.autenticacao.AutenticacaoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.CadastroUsuarioRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.response.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.FuncionarioCopedResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.UsuarioResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.ProfessorResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/login")
@AllArgsConstructor
@CrossOrigin
public class AutenticationController {

    private AutenticacaoService autenticacaoService;
    
    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    @PostMapping
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody LoginRequest authRequest) {
        AuthResponse authResponse = autenticacaoService.login(authRequest);

        return responseService.ok(authResponse);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Response<UsuarioResponse>> cadastrar(@RequestBody CadastroUsuarioRequest cadastroUsuarioDto) {
        Usuario usuario = autenticacaoService.cadastrar(cadastroUsuarioDto);

        UsuarioResponse dto = modelMapperService.convert(usuario, UsuarioResponse.class);

        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/coped/ativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> ativarFuncionarioCoped(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.ativarFuncionarioCoped(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/professor/ativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> ativarProfessor(@PathVariable Long id) {
        Professor professor = autenticacaoService.ativarProfessor(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

    // @GetMapping("/espera-cadastro/coped")
    // public ResponseEntity<Response<List<FuncionarioCopedDto>>> listarEsperaCadastroCoped() {
    //     List<FuncionarioCoped> funcionarioCopeds = autenticacaoService.listarEsperaCadastroCoped();

    //     List<FuncionarioCopedDto> dtos = modelMapperService.convertToList(funcionarioCopeds, FuncionarioCopedDto.class);

    //     return responseService.ok(funcionarioCopeds);
    // }

    // @GetMapping("/espera-cadastro/professor")
    // public ResponseEntity<Response<List<ProfessorDto>>> listarEsperaCadastroProfessor() {
    //     List<Professor> professors = autenticacaoService.listarEsperaCadastroProfessor();

    //     return responseService.ok(professors);
    // }
}
