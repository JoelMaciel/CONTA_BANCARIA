package br.com.banco.domain.exception;

public class NomeOperadorTransacaoNaoEncontradoException extends EntidadeNaoExistException {
    public NomeOperadorTransacaoNaoEncontradoException(String name) {
        super(String.format("Não existe uma conta cadastrada com o Id %d", name));
    }


}

