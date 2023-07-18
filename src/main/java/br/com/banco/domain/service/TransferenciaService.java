package br.com.banco.domain.service;

import br.com.banco.api.dto.TransferenciaDTO;
import br.com.banco.domain.exception.NomeOperadorTransacaoNaoEncontradoException;
import br.com.banco.domain.model.Conta;
import br.com.banco.domain.model.Transferencia;
import br.com.banco.domain.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaService contaService;

    public Page<TransferenciaDTO> obterTransferenciasPorConta(Long idConta,Pageable pageable) {
        contaService.buscarContaPorId(idConta);
        List<Transferencia> transferencias = transferenciaRepository.findByContaIdConta(idConta);
        return mapearTransferencias(transferencias,pageable);
    }

    public Page<TransferenciaDTO> obterTransferenciasPorPeriodo(LocalDate dataInicial, LocalDate dataFinal,Pageable pageable) {
        List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
        return mapearTransferencias(transferencias, pageable);
    }

    public Page<TransferenciaDTO> obterTransferenciasPorOperador(String nomeOperadorTransacao, Pageable pageable) {
        existeNomeOperadorTransacao(nomeOperadorTransacao);
        List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao(nomeOperadorTransacao);
        return mapearTransferencias(transferencias,pageable);
    }

    public Page<TransferenciaDTO> obterTransferenciasPorFiltros(
            Long idConta, LocalDate dataInicial, LocalDate dataFinal, String nomeOperadorTransacao, Pageable pageable) {
        if (idConta != null && dataInicial != null && dataFinal != null && nomeOperadorTransacao != null) {
            contaService.buscarContaPorId(idConta);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndDataTransferenciaBetweenAndNomeOperadorTransacao(
                    idConta, dataInicial, dataFinal, nomeOperadorTransacao);

            return mapearTransferencias(transferencias ,pageable);
        }

        if (idConta != null && dataInicial != null && dataFinal != null) {
            contaService.buscarContaPorId(idConta);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndDataTransferenciaBetween(
                    idConta, dataInicial, dataFinal);
            return mapearTransferencias(transferencias ,pageable);
        }

        if (idConta != null && nomeOperadorTransacao != null) {
            contaService.buscarContaPorId(idConta);
            existeNomeOperadorTransacao(nomeOperadorTransacao);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndNomeOperadorTransacao(idConta, nomeOperadorTransacao);
            return mapearTransferencias(transferencias,pageable);
        }

        if (dataInicial != null && dataFinal != null) {
            List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
            return mapearTransferencias(transferencias,pageable);

        }

        if (nomeOperadorTransacao != null) {
            existeNomeOperadorTransacao(nomeOperadorTransacao);
            List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao(nomeOperadorTransacao);

            return mapearTransferencias(transferencias,pageable);
        }

        if (idConta != null) {
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdConta(idConta);
            return mapearTransferencias(transferencias, pageable);

        }

        List<Transferencia> transferencias = transferenciaRepository.findAll();
        return mapearTransferencias(transferencias, pageable);
    }

    public BigDecimal calcularSaldoTotalPorPeriodo(Long idConta, LocalDate dataInicial, LocalDate dataFinal) {
        Conta conta = contaService.buscarContaPorId(idConta);
        List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetweenAndContaIdConta(dataInicial, dataFinal, idConta);

        BigDecimal saldoTotal = BigDecimal.ZERO;

        for (Transferencia transferencia : transferencias) {
            if (transferencia.getTipo().equals("DEPOSITO") || transferencia.getTipo().equals("TRANSFERENCIA DE ENTRADA")) {
                saldoTotal = saldoTotal.add(transferencia.getValor());
            } else if (transferencia.getTipo().equals("SAQUE") || transferencia.getTipo().equals("TRANSFERENCIA DE SAIDA")) {
                saldoTotal = saldoTotal.subtract(transferencia.getValor());
            }
        }

        return saldoTotal;
    }

    public boolean existeNomeOperadorTransacao(String nomeOperadorTransacao) {
        boolean existeOperador = transferenciaRepository.existsByNomeOperadorTransacao(nomeOperadorTransacao);
        if (!existeOperador) {
            throw new NomeOperadorTransacaoNaoEncontradoException(nomeOperadorTransacao);
        }
        return existeOperador;
    }

    private Page<TransferenciaDTO> mapearTransferencias(List<Transferencia> transferencias, Pageable pageable) {
        List<TransferenciaDTO> transferenciaDTOs = transferencias.stream()
                .map(TransferenciaDTO::toDTO)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), transferenciaDTOs.size());

        return new PageImpl<>(transferenciaDTOs.subList(start, end), pageable, transferenciaDTOs.size());
    }

}