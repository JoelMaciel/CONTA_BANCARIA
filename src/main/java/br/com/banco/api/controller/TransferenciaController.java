package br.com.banco.api.controller;

import br.com.banco.api.controller.openapi.TransferenciaControllerOpenApi;
import br.com.banco.api.dto.TransferenciaDTO;
import br.com.banco.domain.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController implements TransferenciaControllerOpenApi {

    private final TransferenciaService transferenciaService;


    @GetMapping("/{idConta}")
    public List<TransferenciaDTO> obterTransferenciasPorConta(@PathVariable Long idConta) {
        return transferenciaService.obterTransferenciasPorConta(idConta);
    }

    @GetMapping
    public List<TransferenciaDTO> obterTransferenciasPorFiltros(
            @RequestParam(required = false) Long idConta,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal,
            @RequestParam(required = false) String nomeOperadorTransacao) {

        return transferenciaService.obterTransferenciasPorFiltros(idConta, dataInicial, dataFinal, nomeOperadorTransacao);

    }

    @GetMapping("/{idConta}/saldo")
    public BigDecimal calcularSaldoTotalPorPeriodo(
            @PathVariable Long idConta,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal) {

        return transferenciaService.calcularSaldoTotalPorPeriodo(idConta, dataInicial, dataFinal);
    }

}
