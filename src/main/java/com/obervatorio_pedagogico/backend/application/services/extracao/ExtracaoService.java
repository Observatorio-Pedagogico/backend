package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.uploader.Uploader;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FalhaArquivoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.customMultipartFile.CustomMultipartFile;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.rabbitmq.MQConfig;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.LinhaArquivo;
import com.obervatorio_pedagogico.backend.presentation.model.queue.ArquivoQueue;
import com.obervatorio_pedagogico.backend.presentation.model.queue.ExtracaoRequestQueue;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExtracaoService {

    private final ExtracaoRepository extracaoRepository;

    private final AlunoService alunoService;

    private final DisciplinaService disciplinaService;

    private final ModelMapperService modelMapperService;

    private final RabbitTemplate rabbitTemplate;

    public void adicionarNaFila(ExtracaoRequest extracaoRequest) {
        Extracao extracao = modelMapperService.convert(extracaoRequest, Extracao.class);
        extracao.setStatus(Status.AGUARDANDO_PROCESSAMENTO);

        ExtracaoRequestQueue extracaoRequestAux = modelMapperService.convert(extracao, ExtracaoRequestQueue.class);
        ArquivoQueue arquivoQueue;
        try {
            arquivoQueue = new ArquivoQueue(
                extracaoRequest.getArquivo().getConteudo().getBytes(),
                extracaoRequest.getArquivo().getConteudo().getContentType()
            );
        } catch (IOException e) {
            throw new FalhaArquivoException();
        }
        extracaoRequestAux.setArquivo(arquivoQueue);
        rabbitTemplate.convertAndSend(MQConfig.EXTRACAO_EXCHANGE, MQConfig.ROUTING_KEY_ENTRADA, extracaoRequestAux);
    }

    @RabbitListener(queues = {MQConfig.EXTRACAO_QUEUE_ENTRADA})
    public void cadastrar(ExtracaoRequestQueue extracaoRequestQueue) {
        Extracao extracao = modelMapperService.convert(extracaoRequestQueue, Extracao.class);
        ExtracaoRequest extracaoRequest = modelMapperService.convert(extracaoRequestQueue, ExtracaoRequest.class);
        CustomMultipartFile customMultipartFile = new CustomMultipartFile(extracaoRequestQueue.getArquivo().getConteudoArquivo());

        extracaoRequest.getArquivo().setConteudo(customMultipartFile);
        processar(extracao, extracaoRequest.getArquivo());
    }

    public void processar(Extracao extracao, Arquivo arquivo) {
        validarArquivo(arquivo);
        lerArquivo(extracao, arquivo);
    }

    public List<Extracao> getTodos() {
        List<Extracao> extracoes = extracaoRepository.findAll();
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public Extracao getById(Long id){
        Optional<Extracao> extracao = extracaoRepository.findById(id);
        if(!extracao.isPresent()){
            throw new NaoEncontradoException();
        }
        return extracao.get();
    }

    public List<Extracao> getByStatus(Status status){
        List<Extracao> extracoes = extracaoRepository.findByStatus(status);
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public List<Extracao> getByPeriodoLetivo(String periodoLetivo){
        List<Extracao> extracoes = extracaoRepository.findByPeriodoLetivo(periodoLetivo);
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public void deletaExtracao(Long id){
        if (Objects.isNull(id)) {
            throw new NaoEncontradoException("O id da extracao não pode ser null ");
        }

        Extracao extracao = extracaoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("A extracao de ".concat(id.toString()).concat(" não foi encontrado.")));
        List<Disciplina> disciplinasExtracoesRemover = new ArrayList<>(extracao.getDisciplinas());
        disciplinasExtracoesRemover.stream().forEach(disciplina -> {
            disciplina.removeExtracao(extracao);
            extracao.removeDisciplina(disciplina);
            if (disciplina.isPassivoDeletar()) {
                disciplinaService.deleteDisciplina(disciplina);
            }
        });
        
        extracaoRepository.deleteById(extracao.getId());
    }
    
    private void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isSuportado())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }
    
    private void validarArquivo(Arquivo arquivo) {
        validarFormatoArquivo(arquivo);
    }

    private void lerArquivo(Extracao extracao, Arquivo arquivo) {
        try {
            Workbook workbook = WorkbookFactory.create(arquivo.getConteudo().getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            iniciarThread(extracao, sheet);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarThread(Extracao extracao, Sheet sheet) {
        ExtracaoThread thread = new ExtracaoThread();
        thread.setExtracao(extracao);
        Uploader.getInstance().addThread(thread);

        cadastrarExtracao(extracao, sheet, thread);
    }

    private void cadastrarExtracao(Extracao extracao, Sheet sheet, ExtracaoThread extracaoThread) {
        Aluno aluno = null;
        extracaoThread.setTotalLinhas(sheet.getLastRowNum());

        extracao.iniciar();
        extracaoRepository.save(extracao);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            LinhaArquivo linhaArquivo = new LinhaArquivo(sheet.getRow(i));
            if (Objects.isNull(linhaArquivo.getMatricula())) continue;

            Disciplina disciplina = findDisciplina(linhaArquivo, extracao);

            if (
                (i < sheet.getLastRowNum()-1 && Objects.nonNull(linhaArquivo.getMatricula())) &&
                (Objects.isNull(aluno) || !aluno.getMatricula().equals(sheet.getRow(i+1).getCell(1).getStringCellValue()))
            ){
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    aluno = null;
                    extracaoThread.setLinhaAtual(i);
                    continue;
                }
                aluno = findAluno(linhaArquivo, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
        extracao.setStatus(Status.SALVANDO);
        extracaoRepository.save(extracao);
        
        Uploader.getInstance().removeThread(extracao.getId());

        extracao = extracaoRepository.findById(extracao.getId()).get();
        extracao.setStatus(Status.ATIVA);
        extracaoRepository.save(extracao);
        System.out.println(Status.ATIVA);
    }

    private Disciplina findDisciplina(LinhaArquivo linhaArquivo, Extracao extracao) {
        String periodoLetivo = definirPeriodoLetivo(linhaArquivo.getAnoLetivo().toString(), linhaArquivo.getPeriodoLetivo().toString());
        Optional<Disciplina> disciplinaOp = extracao.findDisciplinaByCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        disciplinaOp = disciplinaService.buscarPorCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(linhaArquivo.getCodigoDisciplina());
        disciplina.setNome(linhaArquivo.getNomeDisciplina());
        disciplina.setPeriodoLetivo(periodoLetivo);
        return disciplina;
    }

    private Aluno findAluno(LinhaArquivo linhaArquivo, Extracao extracao) {
        String matricula = linhaArquivo.getMatricula();

        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        Aluno aluno = new Aluno();
        aluno.setMatricula(matricula);
        aluno.setNome(linhaArquivo.getNomeAluno());
        return aluno;
    }

    private String definirPeriodoLetivo(String anoLetivo, String serieLetivo) {
        return anoLetivo.concat(".").concat(serieLetivo);
    }
}
