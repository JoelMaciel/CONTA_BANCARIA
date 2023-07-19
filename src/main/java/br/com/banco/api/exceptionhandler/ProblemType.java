package br.com.banco.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ERRO_SISTEMA("/erro do sistema", "Erro do Sistema"),
    DADOS_INVALIDOS("/invalid-data", "Dados inválidos"),
    PARAMETROS_INVALIDOS("/parâmetro-inválido", "Parâmetro inválido"),
    MESSAGEM_INCOMPREENSIVEL("/mensagem incompreensível", "Mensagem incompreensível"),
    RECURSO_NAO_ENCONTRAD("/recurso-não-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade em uso", "Entidade em Uso"),
    ERRO_DE_NEGOCIO("/business-error", "Violação das Regras de Negócio");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://conta_bancaria.com.br" + path;
        this.title = title;
    }
}
