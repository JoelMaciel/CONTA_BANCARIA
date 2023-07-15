package br.com.banco.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transferencia {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime dataTransferencia ;

    @Column(nullable = false)
    private BigDecimal valor ;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String nomeOperadorTransacao;

    @ManyToOne()
    @JoinColumn(name = "conta_id")
    private Conta conta;


}