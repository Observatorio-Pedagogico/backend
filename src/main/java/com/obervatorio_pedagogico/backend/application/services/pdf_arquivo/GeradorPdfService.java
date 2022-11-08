package com.obervatorio_pedagogico.backend.application.services.pdf_arquivo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
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
        PdfWriter.getInstance(document, byteArrayOutputStream);
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
        adicionarTitulo(arquivoSubParte.getTituloConteudo(), document);

        Font font = FontFactory.getFont(FontFactory.TIMES, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk(arquivoSubParte.getConteudo() + "\n\n", font);
        document.add(new Paragraph(chunk));
    }
    
    private void adicionarImagem(PdfArquivoSubParte arquivoSubParte, Document document) throws DocumentException, MalformedURLException, IOException {
        adicionarTitulo(arquivoSubParte.getTituloConteudo(), document);

        byte[] bytes = base64Service.decode(arquivoSubParte.getConteudo().split(",")[1]);

        Image image = Image.getInstance(bytes);

        document.add(image);
        Chunk chunk = new Chunk("\n");
        document.add(new Paragraph(chunk));
    }

    private void adicionarTitulo(String titulo, Document document) throws DocumentException {
        if (Objects.isNull(titulo)) return;

        Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 25, BaseColor.BLACK);
        font.setStyle("BOLD");
        Chunk chunk = new Chunk(titulo + "\n", font);
        document.add(new Paragraph(chunk));
    }

    private void adicionarTitulo(PdfArquivoSubParte arquivoSubParte, Document document) throws DocumentException {
        adicionarTitulo(arquivoSubParte.getConteudo(), document);
    }
}
