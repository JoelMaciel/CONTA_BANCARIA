package br.com.banco.api.dto;

import br.com.banco.domain.model.Conta;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {

    private Long idConta;

    private String nomeResponsavel;

    public static ContaDTO toDTO(Conta conta) {
        return ContaDTO.builder()
                .idConta(conta.getIdConta())
                .nomeResponsavel(conta.getNomeResponsavel())
                .build();
    }
}
