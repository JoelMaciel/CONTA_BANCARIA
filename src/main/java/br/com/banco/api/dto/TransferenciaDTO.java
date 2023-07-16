package br.com.banco.api.dto;

import br.com.banco.domain.model.Transferencia;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaDTO {

    private Long id;

    private LocalDateTime dataTransferencia ;

    private BigDecimal valor ;

    private String tipo;

    private String nomeOperadorTransacao;

    private ContaDTO conta;

    public static TransferenciaDTO toDTO(Transferencia transferencia) {
        return  TransferenciaDTO.builder()
                .id(transferencia.getId())
                .dataTransferencia(transferencia.getDataTransferencia())
                .valor(transferencia.getValor())
                .tipo(transferencia.getTipo())
                .nomeOperadorTransacao(transferencia.getNomeOperadorTransacao())
                .conta(ContaDTO.toDTO(transferencia.getConta()))
                .build();
    }

}