package br.com.banco.domain.service;

import br.com.banco.api.dto.TransferenciaDTO;
import br.com.banco.domain.exception.NomeOperadorTransacaoNaoEncontradoException;
import br.com.banco.domain.model.Conta;
import br.com.banco.domain.model.Transferencia;
import br.com.banco.domain.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaService contaService;

    public List<TransferenciaDTO> obterTransferenciasPorConta(Long idConta) {
        contaService.buscarContaPorId(idConta);
        List<Transferencia> transferencias = transferenciaRepository.findByContaIdConta(idConta);
        return mapearTransferencias(transferencias);
    }

    public List<TransferenciaDTO> obterTransferenciasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
        return mapearTransferencias(transferencias);
    }

    public List<TransferenciaDTO> obterTransferenciasPorOperador(String nomeOperadorTransacao) {
        existeNomeOperadorTransacao(nomeOperadorTransacao);
        List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao(nomeOperadorTransacao);
        return mapearTransferencias(transferencias);
    }

    public List<TransferenciaDTO> obterTransferenciasPorFiltros(
            Long idConta, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperadorTransacao) {
        existeNomeOperadorTransacao(nomeOperadorTransacao);
        if (idConta != null && dataInicial != null && dataFinal != null && nomeOperadorTransacao != null) {
            contaService.buscarContaPorId(idConta);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndDataTransferenciaBetweenAndNomeOperadorTransacao(
                    idConta, dataInicial, dataFinal, nomeOperadorTransacao);

            return mapearTransferencias(transferencias);
        }

        if (idConta != null && dataInicial != null && dataFinal != null) {
            contaService.buscarContaPorId(idConta);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndDataTransferenciaBetween(
                    idConta, dataInicial, dataFinal);
            return mapearTransferencias(transferencias);
        }

        if (idConta != null && nomeOperadorTransacao != null) {
            contaService.buscarContaPorId(idConta);
            existeNomeOperadorTransacao(nomeOperadorTransacao);
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdContaAndNomeOperadorTransacao(idConta, nomeOperadorTransacao);
            return mapearTransferencias(transferencias);
        }

        if (dataInicial != null && dataFinal != null) {
            List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
            return mapearTransferencias(transferencias);

        }

        if (nomeOperadorTransacao != null) {
            existeNomeOperadorTransacao(nomeOperadorTransacao);
            List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao(nomeOperadorTransacao);

            return mapearTransferencias(transferencias);
        }

        if (idConta != null) {
            List<Transferencia> transferencias = transferenciaRepository.findByContaIdConta(idConta);
            return mapearTransferencias(transferencias);

        }

        List<Transferencia> transferencias = transferenciaRepository.findAll();
        return mapearTransferencias(transferencias);
    }

    public BigDecimal calcularSaldoTotalPorPeriodo(Long idConta, LocalDateTime dataInicial, LocalDateTime dataFinal) {
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

    private List<TransferenciaDTO> mapearTransferencias(List<Transferencia> transferencias) {
        return transferencias.stream()
                .map(TransferenciaDTO::toDTO)
                .toList();
    }

}