package com.obervatorio_pedagogico.backend.application.services.pdf_arquivo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.obervatorio_pedagogico.backend.infrastructure.utils.base64.Base64Service;
import com.obervatorio_pedagogico.backend.presentation.dto.pdf_arquivo.PdfArquivoResponse;
import com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo.PdfArquivo;
import com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo.PdfArquivoSubParte;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GeradorPdfService {

    private Base64Service base64Service;
    
    public PdfArquivoResponse gerarPdf(PdfArquivo pdfArquivo) throws FileNotFoundException, DocumentException, JsonProcessingException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        writer.setStrictImageSequence(true);
        document.open();

        pdfArquivo.getSubPartes().stream().forEach(subParte -> {
            try {
                switch (subParte.getTipo()) {
                    case TEXTO:
                        adicionarTexto(subParte, document);
                        break;
                    case IMAGEM:
                        adicionarImagem(subParte, document);
                        break;
                    case TITULO:
                        adicionarTitulo(subParte, document);
                        break;
                    default:
                        break;
                }
            } catch (DocumentException | IOException documentException) {}
        });
        
        document.close();
        PdfArquivoResponse arquivoResponse = new PdfArquivoResponse();
        arquivoResponse.setConteudo(base64Service.encode(byteArrayOutputStream.toByteArray()));

        return arquivoResponse;
    }

    private void adicionarTexto(PdfArquivoSubParte arquivoSubParte, Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
        paragraph.setFont(font);

        adicionarTitulo(arquivoSubParte.getTituloConteudo(), document);
        paragraph.add(arquivoSubParte.getConteudo() + "\n");

        document.add(paragraph);
    }
    
    private void adicionarImagem(PdfArquivoSubParte arquivoSubParte, Document document) throws DocumentException, MalformedURLException, IOException {
        Paragraph paragraph = new Paragraph(); 
        adicionarTitulo(arquivoSubParte.getTituloConteudo(), document);

        byte[] bytes = base64Service.decode(arquivoSubParte.getConteudo().split(",")[1]);
        Image image = Image.getInstance(bytes);

        image.scaleToFit(PageSize.A4.getWidth() * 0.90f, PageSize.A4.getHeight() * 0.90f);
        image.setAlignment(Image.MIDDLE);
        
        paragraph.add(image);
        document.add(paragraph);
    }

    private void adicionarTitulo(String titulo, Document document) throws DocumentException {
        if (Objects.isNull(titulo)) return;

        Paragraph paragraph = new Paragraph(); 
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);

        paragraph.setFont(font);
        paragraph.add("\n\n" + titulo + "\n");
        
        document.add(paragraph);
    }

    private void adicionarTitulo(PdfArquivoSubParte arquivoSubParte, Document document) throws DocumentException {
        adicionarTitulo(arquivoSubParte.getConteudo(), document);
    }
}
