package br.com.banco.api.controller;

import br.com.banco.api.controller.openapi.TransferenciaControllerOpenApi;
import br.com.banco.api.dto.TransferenciaDTO;
import br.com.banco.domain.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController implements TransferenciaControllerOpenApi {

    private final TransferenciaService transferenciaService;

    @GetMapping("/{idConta}")
    public Page<TransferenciaDTO> obterTransferenciasPorConta(@PathVariable Long idConta, Pageable pageable) {
        return transferenciaService.obterTransferenciasPorConta(idConta,pageable);
    }

    @GetMapping
    public Page<TransferenciaDTO> obterTransferenciasPorFiltros(
            @RequestParam(required = false) Long idConta,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal,
            @RequestParam(required = false) String nomeOperadorTransacao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return transferenciaService.obterTransferenciasPorFiltros(idConta, dataInicial, dataFinal, nomeOperadorTransacao, pageable);
    }


    @GetMapping("/{idConta}/saldo")
    public BigDecimal calcularSaldoTotalPorPeriodo(
            @PathVariable Long idConta,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicial, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFinal,
            Pageable pageable) {

        return transferenciaService.calcularSaldoTotalPorPeriodo(idConta, dataInicial, dataFinal);
    }

}
