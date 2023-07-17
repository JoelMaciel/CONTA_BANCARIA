package br.com.banco.domain.repository;

import br.com.banco.domain.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByContaIdConta(Long idConta);

    List<Transferencia> findByDataTransferenciaBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<Transferencia> findByNomeOperadorTransacao(String nomeOperadorTransacao);

    List<Transferencia> findByContaIdContaAndDataTransferenciaBetween(
            Long idConta, LocalDate dataInicial, LocalDate dataFinal);

    List<Transferencia> findByContaIdContaAndDataTransferenciaBetweenAndNomeOperadorTransacao(
            Long idConta, LocalDate dataInicial, LocalDate dataFinal, String nomeOperadorTransacao);


    List<Transferencia> findByContaIdContaAndNomeOperadorTransacao(Long idConta, String nomeOperadorTransacao);

    List<Transferencia> findByDataTransferenciaBetweenAndContaIdConta(LocalDate dataInicial, LocalDate dataFinal, Long contaId);

    boolean existsByNomeOperadorTransacao(String nomeOperadorTransacao);
}
