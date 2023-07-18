package br.com.banco.domain.controller;

import br.com.banco.domain.repository.TransferenciaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class TransferenciaControllerIT {

    private static final String CONTA_NAO_EXISTE = "Não existe uma conta cadastrada com o Id 3";
    private static final String DATAS_INEXISTENTES ="Error: Você deve preencher os campos Data Inicial e Data Final. ";

    @LocalServerPort
    private int port;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    private int quantidadeTotalTransferencia;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/api/transferencias";
        quantidadeTotalTransferencia = (int) transferenciaRepository.count();
    }

    @Test
    public void deveRetornarStatus200EQuantidadeDeTransferenciasCorretas_QuandoConsultarTranferenciaSemParametros() {
        given()
                .accept(ContentType.JSON)
              .when()
                .get()
              .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(quantidadeTotalTransferencia));

    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarTransfereciaDeIdContaInexistente() {
        given()
                .pathParams("idConta", 3L)
              .when()
                .get("/{idConta}")
              .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("detail", equalTo(CONTA_NAO_EXISTE));
    }
    @Test
    public void deveRetornarStatus200_QuandoBuscarTransferenciaPorNomeOperadorTransacao() {
        String nomeOperadorTransacao = "Ronnyscley";

        given()
                .accept(ContentType.JSON)
                .param("nomeOperadorTransacao", nomeOperadorTransacao)
              .when()
                .get()
              .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(6));
    }

    @Test
    public void deveRetornarStatus200EQuantidadeCorretaDeTranferencias_QuandoConsultarTransferenciaEntreDatas() {

        LocalDate dataInicial = LocalDate.of(2019, 1, 20);
        LocalDate dataFinal = LocalDate.of(2020, 12, 15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataInicialStr = dataInicial.format(formatter);
        String dataFinalStr = dataFinal.format(formatter);

        given()
                .accept(ContentType.JSON)
                .param("dataInicial", dataInicialStr)
                .param("dataFinal", dataFinalStr)
              .when()
                .get()
              .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(8));
    }
    @Test
    public void deveRetornarStatus200ESaldorCorreto_QuandoConsultarSaldoDaContaEntreDatas() {
        Long idConta = 1L;
        LocalDate dataInicial = LocalDate.of(2019, 1, 20);
        LocalDate dataFinal = LocalDate.of(2020, 12, 15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataInicialStr = dataInicial.format(formatter);
        String dataFinalStr = dataFinal.format(formatter);

        given()
                .accept(ContentType.JSON)
                .pathParams("idConta", idConta)
                .param("dataInicial", dataInicialStr)
                .param("dataFinal", dataFinalStr)
              .when()
                .get("/{idConta}/saldo")
              .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("2196.46"));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarSaldoDeIdContaInxistente() {
        Long idConta = 3L;
        LocalDate dataInicial = LocalDate.of(2019, 1, 20);
        LocalDate dataFinal = LocalDate.of(2020, 12, 15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataInicialStr = dataInicial.format(formatter);
        String dataFinalStr = dataFinal.format(formatter);

        given()
                .accept(ContentType.JSON)
                .pathParams("idConta", idConta)
                .param("dataInicial", dataInicialStr)
                .param("dataFinal", dataFinalStr)
              .when()
                .get("/{idConta}/saldo")
              .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("userMessage", equalTo(CONTA_NAO_EXISTE));
    }

    @Test
    public void deveRetornarStatus400_QuandoConsultarSaldoSemDefinirDatas() {
        Long idConta = 1L;

        given()
                .accept(ContentType.JSON)
                .pathParams("idConta", idConta)
              .when()
                .get("/{idConta}/saldo")
              .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("userMessage", equalTo(DATAS_INEXISTENTES));
    }
}
