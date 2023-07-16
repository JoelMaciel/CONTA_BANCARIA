package br.com.banco.domain.service;

import br.com.banco.domain.exception.ContaNaoExisteException;
import br.com.banco.domain.model.Conta;
import br.com.banco.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public Conta buscarContaPorId(Long idConta) {
        return contaRepository.findById(idConta)
                .orElseThrow(() -> new ContaNaoExisteException(idConta));
    }
}
