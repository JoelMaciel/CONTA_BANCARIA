package br.com.banco.domain.repository;

import br.com.banco.domain.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByContaIdConta(Long idConta);

    List<Transferencia> findByDataTransferenciaBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<Transferencia> findByNomeOperadorTransacao(String nomeOperadorTransacao);

    List<Transferencia> findByContaIdContaAndDataTransferenciaBetween(
            Long idConta, LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<Transferencia> findByContaIdContaAndDataTransferenciaBetweenAndNomeOperadorTransacao(
            Long idConta, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperadorTransacao);


    List<Transferencia> findByContaIdContaAndNomeOperadorTransacao(Long idConta, String nomeOperadorTransacao);

    List<Transferencia> findByDataTransferenciaBetweenAndContaIdConta(LocalDateTime dataInicial, LocalDateTime dataFinal, Long contaId);

    boolean existsByNomeOperadorTransacao(String nomeOperadorTransacao);
}
