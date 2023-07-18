package br.com.banco.domain.service;

import br.com.banco.api.dto.TransferenciaDTO;
import br.com.banco.domain.exception.ContaNaoExisteException;
import br.com.banco.domain.repository.TransferenciaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TransferenciaServiceTest {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private ContaService contaService;
    @Autowired
    private TransferenciaService transferenciaService;

    @Test
    public void deveRetornarQuantidadeDeTransferenciaDaIdConta2() {

        List<TransferenciaDTO> transferencias = transferenciaService.obterTransferenciasPorConta(2L);

        assertNotNull(transferencias);
        assertEquals(6, transferencias.size());

    }

    @Test
    public void deveRetornarOsDadosCorretosDaPrimeiraTransferenciaDaListaDeIdConta2() {

        List<TransferenciaDTO> transferencias = transferenciaService.obterTransferenciasPorConta(2L);

        TransferenciaDTO primeiraTransferencia = transferencias.get(0);

        assertThat(primeiraTransferencia.getId()).isEqualTo(2);
        assertThat(primeiraTransferencia.getDataTransferencia()).isEqualTo(LocalDate.of(2019, 2, 3));
        assertThat(primeiraTransferencia.getValor()).isEqualByComparingTo(BigDecimal.valueOf(12.24));
        assertThat(primeiraTransferencia.getTipo()).isEqualTo("DEPOSITO");
        assertThat(primeiraTransferencia.getNomeOperadorTransacao()).isEqualTo("Ronnyscley");
        assertThat(primeiraTransferencia.getConta().getIdConta()).isEqualTo(2);
        assertThat(primeiraTransferencia.getConta().getNomeResponsavel()).isEqualTo("Sicrano");
        assertNotNull(transferencias);

    }

    @Test
    public void deveRetornar404_QuandoInformarIdContaInexistente() {

        ContaNaoExisteException expectedError = Assertions.assertThrows(ContaNaoExisteException.class,
                () -> {
                    transferenciaService.obterTransferenciasPorConta(3L);
                });
        assertThat(expectedError).isNotNull();
        assertEquals("Não existe uma conta cadastrada com o Id 3", expectedError.getMessage());

    }

    @Test
    public void deveRetornarListaDeTransferenciasPorPeriodoInformado() {

        List<TransferenciaDTO> transferencias = transferenciaService.obterTransferenciasPorPeriodo(
                LocalDate.of(2019, 01, 10), LocalDate.of(2021, 12, 30));

        assertNotNull(transferencias);

    }

    @Test
    public void deveCalcularSaldoTotalPorPeriodoCorretamente() {
        Long idConta = 1L;
        LocalDate dataInicial = LocalDate.of(2020, 1, 1);
        LocalDate dataFinal = LocalDate.of(2021, 12, 31);

        BigDecimal saldoTotal = transferenciaService.calcularSaldoTotalPorPeriodo(idConta, dataInicial, dataFinal);

        assertNotNull(saldoTotal);
        assertEquals(BigDecimal.valueOf(2056.19), saldoTotal);
    }

}

