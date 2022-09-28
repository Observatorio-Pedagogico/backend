package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.uploader.Uploader;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FalhaArquivoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao;
import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao.SituacaoDisciplina;
import com.obervatorio_pedagogico.backend.domain.model.customMultipartFile.CustomMultipartFile;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota.Tipo;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.rabbitmq.MQConfig;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.ArquivoAluno;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.ArquivoDisciplina;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoAluno;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoDisciplina;
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
        ArquivoQueue arquivoDisciplinaQueue;
        ArquivoQueue arquivoAlunoQueue;
        
        try {
            arquivoDisciplinaQueue = new ArquivoQueue(
                extracaoRequest.getArquivoDisciplina().getConteudo().getBytes(),
                extracaoRequest.getArquivoDisciplina().getConteudo().getContentType()
            );
        } catch (NullPointerException nullPointerException) {
            arquivoDisciplinaQueue = null;
        } catch (IOException e) {
            throw new FalhaArquivoException();
        }

        try {
            arquivoAlunoQueue = new ArquivoQueue(
                extracaoRequest.getArquivoAluno().getConteudo().getBytes(),
                extracaoRequest.getArquivoAluno().getConteudo().getContentType()
            );
        } catch (NullPointerException nullPointerException) {
            arquivoAlunoQueue = null;
        } catch (IOException e) {
            throw new FalhaArquivoException();
        }

        extracaoRequestAux.setArquivoDisciplina(arquivoDisciplinaQueue);
        extracaoRequestAux.setArquivoAluno(arquivoAlunoQueue);
        rabbitTemplate.convertAndSend(MQConfig.EXTRACAO_EXCHANGE, MQConfig.ROUTING_KEY_ENTRADA, extracaoRequestAux);
    }

    @RabbitListener(queues = {MQConfig.EXTRACAO_QUEUE_ENTRADA})
    public void cadastrar(ExtracaoRequestQueue extracaoRequestQueue) {
        Extracao extracao = modelMapperService.convert(extracaoRequestQueue, Extracao.class);
        ExtracaoRequest extracaoRequest = modelMapperService.convert(extracaoRequestQueue, ExtracaoRequest.class);

        try {
            CustomMultipartFile customMultipartFileArquivoDisciplina = new CustomMultipartFile(extracaoRequestQueue.getArquivoDisciplina().getConteudoArquivo());
            extracaoRequest.getArquivoDisciplina().setConteudo(customMultipartFileArquivoDisciplina);

        } catch (NullPointerException nullPointerException) {}
        
        try {
            CustomMultipartFile customMultipartFileArquivoAluno = new CustomMultipartFile(extracaoRequestQueue.getArquivoAluno().getConteudoArquivo());
            extracaoRequest.getArquivoAluno().setConteudo(customMultipartFileArquivoAluno);

        } catch (NullPointerException nullPointerException) {}

        processar(extracao, extracaoRequest.getArquivoDisciplina(), extracaoRequest.getArquivoAluno());
    }

    public void processar(Extracao extracao, Arquivo arquivoDisciplina, Arquivo arquivoAluno) {
        validar(arquivoDisciplina, arquivoAluno);
        lerArquivo(extracao, arquivoDisciplina, arquivoAluno);
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

    private void validar(Arquivo arquivoDisciplina, Arquivo arquivoAluno) {
        if (Objects.isNull(arquivoDisciplina) || Objects.isNull(arquivoAluno)) {
            //TODO exception aqui
        }

        if (Objects.nonNull(arquivoDisciplina))
            validarArquivoDisciplina(arquivoDisciplina);
        if (Objects.nonNull(arquivoAluno))
            validarArquivoAluno(arquivoAluno);
    }
    
    private void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isSuportado())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }
    
    private void validarArquivoDisciplina(Arquivo arquivo) {
        validarFormatoArquivo(arquivo);
    }

    private void validarArquivoAluno(Arquivo arquivo) {
        validarFormatoArquivo(arquivo);
    }

    private void lerArquivo(Extracao extracao, Arquivo arquivoDisciplina, Arquivo arquivoAluno) {
        try {
            Workbook workbookDisciplina = null;
            Workbook workbookAluno = null;
            Sheet sheetDisciplina = null;
            Sheet sheetAluno = null;

            if (Objects.nonNull(arquivoDisciplina)) {
                workbookDisciplina = WorkbookFactory.create(arquivoDisciplina.getConteudo().getInputStream());
                sheetDisciplina = workbookDisciplina.getSheetAt(0);
            }
            if (Objects.nonNull(arquivoAluno)) {
                workbookAluno = WorkbookFactory.create(arquivoAluno.getConteudo().getInputStream());
                sheetAluno = workbookAluno.getSheetAt(0);
            }
            
            iniciarThread(extracao, sheetDisciplina, sheetAluno);

            if (Objects.nonNull(workbookDisciplina))
                workbookDisciplina.close();
            if (Objects.nonNull(workbookAluno))
                workbookAluno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void iniciarThread(Extracao extracao, Sheet sheetDisciplina, Sheet sheetAluno) {
        ExtracaoThread thread = new ExtracaoThread();
        thread.setExtracao(extracao);
        Uploader.getInstance().addThread(thread);

        thread.setTotalLinhas(obterTotalLinhas(sheetDisciplina) + obterTotalLinhas(sheetAluno));
        iniciarOperacao(extracao);
        cadastrarExtracaoDisciplina(extracao, sheetDisciplina, thread);
        cadastrarExtracaoAluno(extracao, sheetAluno, thread);
        salvarOperacao(extracao);
    }

    private void cadastrarExtracaoDisciplina(Extracao extracao, Sheet sheet, ExtracaoThread extracaoThread) {
        if (Objects.isNull(sheet)) return;

        Aluno aluno = null;
        ArquivoDisciplina arquivoDisciplina = new ArquivoDisciplina(sheet);
        Integer threadQuantidadeLinhas = extracaoThread.getLinhaAtual();

        for (int i = 0; i < arquivoDisciplina.getLinhas().size(); i++) {
            LinhaArquivoDisciplina linhaArquivo = arquivoDisciplina.getLinhas().get(i);
            Disciplina disciplina = findDisciplina(linhaArquivo, extracao);

            if (Objects.isNull(aluno) || arquivoDisciplina.proximoEMesmoAluno(i)) {
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
                    aluno = null;
                    extracaoThread.setLinhaAtual(threadQuantidadeLinhas + i);
                    continue;
                }
                aluno = findAluno(linhaArquivo, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
                gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
    }

    private void cadastrarExtracaoAluno(Extracao extracao, Sheet sheet, ExtracaoThread extracaoThread) {
        if (Objects.isNull(sheet)) return;

        Aluno aluno = null;
        ArquivoAluno arquivoAluno = new ArquivoAluno(sheet);
        Integer threadQuantidadeLinhas = extracaoThread.getLinhaAtual();

        for (int i = 0; i < arquivoAluno.getLinhas().size(); i++) {
            LinhaArquivoAluno linhaArquivo = arquivoAluno.getLinhas().get(i);
            Disciplina disciplina = findDisciplina(linhaArquivo, extracao);

            if (Objects.isNull(aluno) || arquivoAluno.proximoEMesmoAluno(i)) {
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
                    aluno = null;
                    extracaoThread.setLinhaAtual(threadQuantidadeLinhas + i);
                    continue;
                }
                aluno = findAluno(linhaArquivo, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
                gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
    }

    private Disciplina findDisciplina(LinhaArquivoDisciplina linhaArquivo, Extracao extracao) {
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

    private Disciplina findDisciplina(LinhaArquivoAluno linhaArquivo, Extracao extracao) {
        String periodoLetivo = linhaArquivo.getPeriodo();
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

    private Aluno findAluno(LinhaArquivoDisciplina linhaArquivo, Extracao extracao) {
        String matricula = linhaArquivo.getMatricula();

        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(matricula);
        if (alunoOp.isPresent()) {
            Aluno aluno = alunoOp.get();
            aluno.setNome(linhaArquivo.getNomeAluno());
            return aluno;
        }

        Aluno aluno = new Aluno();
        aluno.setMatricula(matricula);
        aluno.setNome(linhaArquivo.getNomeAluno());
        return aluno;
    }

    private Aluno findAluno(LinhaArquivoAluno linhaArquivo, Extracao extracao) {
        String matricula = linhaArquivo.getMatricula();

        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        Aluno aluno = new Aluno();
        aluno.setMatricula(matricula);
        return aluno;
    }

    private void gerarFrequenciaSituacao(Aluno aluno, Disciplina disciplina, LinhaArquivoDisciplina linhaArquivoDisciplina) {
        FrequenciaSituacao frequenciaSituacao = buscarFrequenciaSituacao(aluno, disciplina);
        if (Objects.isNull(frequenciaSituacao)) {
            frequenciaSituacao = new FrequenciaSituacao();
            frequenciaSituacao.setDisciplina(disciplina);
            frequenciaSituacao.setAluno(aluno);
            aluno.addFrequenciaSituacoes(frequenciaSituacao);
            disciplina.addFrequenciaSituacoes(frequenciaSituacao);
        }

        frequenciaSituacao.setSituacaoDisciplina(SituacaoDisciplina.fromString(linhaArquivoDisciplina.getSituacao()));
    }

    private void gerarFrequenciaSituacao(Aluno aluno, Disciplina disciplina, LinhaArquivoAluno linhaArquivoAluno) {
        FrequenciaSituacao frequenciaSituacao = buscarFrequenciaSituacao(aluno, disciplina);
        if (Objects.isNull(frequenciaSituacao)) {
            frequenciaSituacao = new FrequenciaSituacao();
            frequenciaSituacao.setDisciplina(disciplina);
            frequenciaSituacao.setAluno(aluno);
            aluno.addFrequenciaSituacoes(frequenciaSituacao);
            disciplina.addFrequenciaSituacoes(frequenciaSituacao);
        }
        frequenciaSituacao.setFrequencia(linhaArquivoAluno.getFrequencia());

        List<Nota> notas = gerarNotas(linhaArquivoAluno.getNotas());
        for (Nota nota : notas) {
            nota.setAluno(aluno);
            nota.setDisciplina(disciplina);
        }
        frequenciaSituacao.setNotas(notas);
    }

    public FrequenciaSituacao buscarFrequenciaSituacao(Aluno aluno,  Disciplina disciplina) {
        return aluno.getFrequenciaSituacoes().stream()
            .filter(frequenciaSituacao -> frequenciaSituacao.getDisciplina().getCodigo().equals(disciplina.getCodigo()) &&
                frequenciaSituacao.getDisciplina().getPeriodoLetivo().equals(disciplina.getPeriodoLetivo()))
            .findFirst()
            .orElse(null);
    }

    private List<Nota> gerarNotas(String jsonNotas) {
        List<Nota> notas = new ArrayList<>();
        Nota nota = null;

        List<String> jsonSplitNotas = Arrays.asList(jsonNotas.split(","));

        for (String jsonSplitNota : jsonSplitNotas) {
            List<String> notaIndividual = Arrays.asList(jsonSplitNota.split(":"));

            nota = new Nota();
            switch (notaIndividual.get(0).charAt(0)) {
                case 'A':
                    nota.setTipo(Tipo.NOTA);
                    break;
                case 'M':
                    nota.setTipo(Tipo.MEDIA);
                    break;
                case 'F':
                    nota.setTipo(Tipo.FINAL);
                    break;
            }

            String ordem = notaIndividual.get(0).substring(1);
            if (!ordem.isEmpty())
                nota.setOrdem(new Integer(ordem));
            nota.setValor(new BigDecimal(notaIndividual.get(1)));

            notas.add(nota);
        }
        
        return notas;
    }

    private String definirPeriodoLetivo(String anoLetivo, String serieLetivo) {
        return anoLetivo.concat(".").concat(serieLetivo);
    }

    private void iniciarOperacao(Extracao extracao) {
        extracao.iniciar();
        extracaoRepository.save(extracao);
    }

    private void salvarOperacao(Extracao extracao) {
        extracao.setStatus(Status.SALVANDO);
        extracaoRepository.save(extracao);
        
        Uploader.getInstance().removeThread(extracao.getId());

        extracao = extracaoRepository.findById(extracao.getId()).get();
        extracao.setStatus(Status.ATIVA);
        extracaoRepository.save(extracao);
        System.out.println(Status.ATIVA);
    }

    private Integer obterTotalLinhas(Sheet sheet) {
        if (Objects.isNull(sheet)) {
            return 0;
        }
        return sheet.getLastRowNum();
    }
}
