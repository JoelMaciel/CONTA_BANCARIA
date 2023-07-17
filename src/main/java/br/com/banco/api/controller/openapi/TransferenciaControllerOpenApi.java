package br.com.banco.api.controller.openapi;

import br.com.banco.api.dto.TransferenciaDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "Contas Bancárias")
public interface TransferenciaControllerOpenApi {

    @ApiOperation("Buscar transferência por conta bancária.")
    List<TransferenciaDTO> obterTransferenciasPorConta(@PathVariable Long idConta);

    @ApiOperation("Busca transferencia entre datas (ex: 2019-10-24), por nome do operador ou conta bancária.")
    List<TransferenciaDTO> obterTransferenciasPorFiltros(
            @RequestParam(required = false) Long idConta,
            @RequestParam(required = false) LocalDate dataInicial,
            @RequestParam(required = false) LocalDate dataFinal,
            @RequestParam(required = false) String nomeOperadorTransacao);


    @ApiOperation("Busca o sado da conta entre datas fornecidas, ex: 2019-10-24 e 2020-01-15")
    public BigDecimal calcularSaldoTotalPorPeriodo(
            @PathVariable Long idConta,
            LocalDate dataInicial,
            LocalDate dataFinal);
}
