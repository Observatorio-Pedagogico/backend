package com.obervatorio_pedagogico.backend.application.services.extracao;

import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoModel;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExtracaoService {

    private ExtracaoRepository extracaoRepository;

    private ModelMapperService modelMapperService;

    public ExtracaoService(
        ExtracaoRepository extracaoRepository,
        ModelMapperService modelMapperService
    ) {
        this.extracaoRepository = extracaoRepository;
        this.modelMapperService = modelMapperService;
    }
    
    public ExtracaoModel enviar(ExtracaoRequest extracaoRequest) {
        ExtracaoModel extracao = modelMapperService.convert(extracaoRequest, ExtracaoModel.class);
        lerArquivo(extracaoRequest.getArquivo());
        return extracaoRepository.save(extracao);
    }

    public void processar() {
        
    }

    public void lerArquivo(MultipartFile arquivo) {

    }
}
