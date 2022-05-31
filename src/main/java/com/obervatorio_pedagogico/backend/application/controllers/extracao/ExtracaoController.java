package com.obervatorio_pedagogico.backend.application.controllers.extracao;

import com.obervatorio_pedagogico.backend.application.services.extracao.ExtracaoService;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/extracao")
@AllArgsConstructor
public class ExtracaoController {

    private ExtracaoService extracaoService;

    private ModelMapperService modelMapperService;

    private ResponseService responseService;
    
    @PostMapping("/enviar")
    public ResponseEntity<Response<ExtracaoResponse>> enviar(ExtracaoRequest extracaoRequest) {
        Extracao extracaoModel = extracaoService.cadastrar(extracaoRequest);
        ExtracaoResponse extracaoResponse = modelMapperService.convert(extracaoModel, ExtracaoResponse.class);
        return responseService.create(extracaoResponse);
    }
}
