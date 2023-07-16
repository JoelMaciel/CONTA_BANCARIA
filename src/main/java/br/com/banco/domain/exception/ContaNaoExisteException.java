package br.com.banco.domain.exception;

public class ContaNaoExisteException extends EntidadeNaoExistException{

    public ContaNaoExisteException(String message) {
        super(message);
    }

    public ContaNaoExisteException(Long idConta) {
        this(String.format("Não existe uma conta cadastrada com o Id %d", idConta));
    }


}
