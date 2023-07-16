package br.com.banco.domain.exception;

public class EntidadeNaoExistException extends RuntimeException{
    public EntidadeNaoExistException(String message) {
        super(message);
    }
}
