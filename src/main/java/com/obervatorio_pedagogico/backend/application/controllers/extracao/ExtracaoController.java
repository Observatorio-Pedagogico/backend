package com.obervatorio_pedagogico.backend.application.controllers.extracao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.extracao.ExtracaoService;
import com.obervatorio_pedagogico.backend.application.services.extracao.ExtracaoThreadService;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoResponseResumido;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoThreadResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/extracao")
@AllArgsConstructor
public class ExtracaoController {

    private ExtracaoService extracaoService;

    private ExtracaoThreadService extracaoThreadService;

    private ModelMapperService modelMapperService;

    private ResponseService responseService;
    
    @PostMapping("/enviar")
    public ResponseEntity<Response<ExtracaoResponse>> enviar(ExtracaoRequest extracaoRequest) {
        extracaoService.cadastrar(extracaoRequest);
        return responseService.create(null);
    }

    @GetMapping("/envio-status/{id}")
    public ResponseEntity<Response<ExtracaoThreadResponse>> getEnvioStatus(@PathVariable Long id){
        ExtracaoThread extracaoThread = extracaoThreadService.getById(id);

        ExtracaoThreadResponse response = modelMapperService.convert(extracaoThread, ExtracaoThreadResponse.class);
        
        return responseService.ok(response);
    }

    @GetMapping("/envio-status")
    public ResponseEntity<Response<List<ExtracaoThreadResponse>>> getEnvioStatus(){
        List<ExtracaoThread> extracaoThreadList = extracaoThreadService.getAll();

        List<ExtracaoThreadResponse> responseList = new ArrayList<>();
        extracaoThreadList.stream().forEach(thread -> responseList.add(modelMapperService.convert(thread, ExtracaoThreadResponse.class)));
        
        return responseService.ok(responseList);
    }

    @GetMapping("/get-todos")
    public ResponseEntity<Response<List<ExtracaoResponseResumido>>> getTodos(){
        List<Extracao> extracoes = extracaoService.getTodos();
        
        return responseService.ok(extracoes.stream()
        .map(element -> modelMapperService
            .convert(element, ExtracaoResponseResumido.class))
        .collect(Collectors.toList()));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Response<ExtracaoResponseResumido>> getById(@PathVariable Long id){
        Extracao extracao = extracaoService.getById(id);

        return responseService.ok(modelMapperService.convert(extracao, ExtracaoResponseResumido.class));
    }

    @GetMapping("/get-by-status")
    public ResponseEntity<Response<List<ExtracaoResponseResumido>>> getByStatus(Status status){
        List<Extracao> extracoes = extracaoService.getByStatus(status);

        return responseService.ok(extracoes.stream()
        .map(element -> modelMapperService
            .convert(element, ExtracaoResponseResumido.class))
        .collect(Collectors.toList()));
    }

    @GetMapping("/get-by-periodo-letivo")
    public ResponseEntity<Response<List<ExtracaoResponseResumido>>> getByPeriodoLetivo(String periodoLetivo) {
        List<Extracao> extracoes = extracaoService.getByPeriodoLetivo(periodoLetivo);

        return responseService.ok(extracoes.stream()
        .map(element -> modelMapperService
            .convert(element, ExtracaoResponseResumido.class))
        .collect(Collectors.toList()));
    }

    @DeleteMapping
    public ResponseEntity<Response<String>> deletaExtracao(Long id){
        extracaoService.deletaExtracao(id);
        return responseService.ok("Extracao deletada com sucesso.");
    }
}
