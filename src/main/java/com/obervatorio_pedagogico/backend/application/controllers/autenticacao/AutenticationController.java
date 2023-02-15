package com.obervatorio_pedagogico.backend.application.controllers.autenticacao;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.ProfessorResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.UsuarioResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/autenticacao")
@AllArgsConstructor
@CrossOrigin
public class AutenticationController {

    private AutenticacaoService autenticacaoService;
    
    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody @Valid LoginRequest authRequest) {
        AuthResponse authResponse = autenticacaoService.login(authRequest);

        return responseService.ok(authResponse);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Response<UsuarioResponse>> cadastrar(@RequestBody @Valid CadastroUsuarioRequest cadastroUsuarioDto) {
        Usuario usuario = autenticacaoService.cadastrar(cadastroUsuarioDto);

        UsuarioResponse dto = modelMapperService.convert(usuario, UsuarioResponse.class);

        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/coped/ativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> ativarFuncionarioCopedEsperaCadastro(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.ativarFuncionarioCopedEsperaCadastro(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/coped/desativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> desativarFuncionarioCopedEsperaCadastro(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.desativarFuncionarioCopedEsperaCadastro(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/professor/ativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> ativarProfessorEsperaCadastro(@PathVariable Long id) {
        Professor professor = autenticacaoService.ativarProfessorEsperaCadastro(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/professor/desativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> desativarProfessorEsperaCadastro(@PathVariable Long id) {
        Professor professor = autenticacaoService.desativarProfessorEsperaCadastro(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/coped/ativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> ativarFuncionarioCoped(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.ativarFuncionarioCoped(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/coped/desativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> desativarFuncionarioCoped(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.desativarFuncionarioCoped(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/professor/ativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> ativarProfessor(@PathVariable Long id) {
        Professor professor = autenticacaoService.ativarProfessor(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/professor/desativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> desativarProfessor(@PathVariable Long id) {
        Professor professor = autenticacaoService.desativarProfessor(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

    @GetMapping("/espera-cadastro/coped")
    public ResponseEntity<Response<Page<FuncionarioCopedResponse>>> listarEsperaCadastroCoped(Pageable pageable) {
        Page<FuncionarioCoped> funcionarioCopeds = autenticacaoService.listarCopedEsperaCadastro(pageable);

        return responseService.ok(modelMapperService.convert(funcionarioCopeds, FuncionarioCopedResponse.class));
    }

    @GetMapping("/espera-cadastro/professor")
    public ResponseEntity<Response<Page<ProfessorResponse>>> listarEsperaCadastroProfessor(Pageable pageable) {
        Page<Professor> professors = autenticacaoService.listarProfessorEsperaCadastro(pageable);

        return responseService.ok(modelMapperService.convert(professors, ProfessorResponse.class));
    }

    @GetMapping("/coped")
    public ResponseEntity<Response<Page<FuncionarioCopedResponse>>> listarCoped(Pageable pageable) {
        Page<FuncionarioCoped> funcionarioCopeds = autenticacaoService.listarCoped(pageable);

        return responseService.ok(modelMapperService.convert(funcionarioCopeds, FuncionarioCopedResponse.class));
    }

    @GetMapping("/professor")
    public ResponseEntity<Response<Page<ProfessorResponse>>> listarProfessor(Pageable pageable) {
        Page<Professor> professors = autenticacaoService.listarProfessor(pageable);

        return responseService.ok(modelMapperService.convert(professors, ProfessorResponse.class));
    }
}
