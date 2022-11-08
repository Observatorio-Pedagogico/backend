package com.obervatorio_pedagogico.backend.application.controllers.pdf_arquivo;

import java.io.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import com.obervatorio_pedagogico.backend.application.services.pdf_arquivo.GeradorPdfService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.pdf_arquivo.PdfArquivoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.pdf_arquivo.PdfArquivoResponse;
import com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo.PdfArquivo;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/gerador_pdf")
@AllArgsConstructor
@CrossOrigin
public class GeradorPdfController {

    private GeradorPdfService geradorPdfService;

    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    @PostMapping
    public ResponseEntity<Response<PdfArquivoResponse>> gerarPdf(@RequestBody PdfArquivoRequest request) throws FileNotFoundException, DocumentException, JsonProcessingException {
        PdfArquivo pdfArquivo = modelMapperService.convert(request, PdfArquivo.class);

        PdfArquivoResponse response = geradorPdfService.gerarPdf(pdfArquivo);

        return responseService.ok(response);
    }
}
