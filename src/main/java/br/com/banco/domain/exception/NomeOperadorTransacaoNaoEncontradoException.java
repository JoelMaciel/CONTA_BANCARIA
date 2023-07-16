package br.com.banco.domain.exception;

public class NomeOperadorTransacaoNaoEncontradoException extends EntidadeNaoExistException {
    public NomeOperadorTransacaoNaoEncontradoException(String name) {
        super(String.format("Não existe um operador da transação com esse nome: %s", name));
    }


}

