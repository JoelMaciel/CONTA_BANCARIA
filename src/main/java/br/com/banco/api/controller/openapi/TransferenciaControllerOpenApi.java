package br.com.banco.api.controller.openapi;

import br.com.banco.api.dto.TransferenciaDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Api(tags = "Contas Bancárias")
public interface TransferenciaControllerOpenApi {

    @ApiOperation("Buscar transferência por conta bancária.")
    Page<TransferenciaDTO> obterTransferenciasPorConta(@PathVariable Long idConta, Pageable pageable);

    @ApiOperation("Busca transferencia entre datas (ex: 2019-10-24), por nome do operador ou conta bancária.")
    Page<TransferenciaDTO> obterTransferenciasPorFiltros(
            @RequestParam(required = false) Long idConta,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal,
            @RequestParam(required = false) String nomeOperadorTransacao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size);


    @ApiOperation("Busca o sado da conta entre datas fornecidas, ex: 2019-10-24 e 2020-01-15")
    BigDecimal calcularSaldoTotalPorPeriodo(
            @PathVariable Long idConta,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal,
            Pageable pageable);
}
