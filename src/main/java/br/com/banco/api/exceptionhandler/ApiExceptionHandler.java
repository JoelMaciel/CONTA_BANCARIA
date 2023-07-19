package br.com.banco.api.exceptionhandler;

import br.com.banco.domain.exception.EntidadeNaoExistException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_GENERIC_ERROR_END_USER = "Ocorreu um erro de sistema interno inesperado."
            + "Tente novamente e se o problema persistir entre em contato conosco com o administrador do sistema.";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_SISTEMA;
        String detail = MSG_GENERIC_ERROR_END_USER;
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeNaoExistException.class)
    public ResponseEntity<?> handleEntityNotFound(EntidadeNaoExistException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRAD;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(InvalidDataAccessApiUsageException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Error: Você deve preencher os campos Data Inicial e Data Final. ";

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos são inválidos. Preencha o formulário correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Problem.Object> problemObjects = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    return Problem.Object.builder()
                            .name(fieldError.getField())
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRAD;
        String detail = String.format("O recurso %s que você tentou acessar não existe.",
                ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                        HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.PARAMETROS_INVALIDOS;

        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e insira um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.MESSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da solicitação é inválido. Verifique o erro de sintaxe";

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MESSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e digite um valor compatível com o tipo %s ",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MESSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe."
                + "Corrija ou remova esta propriedade e tente novamente.", path);
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_GENERIC_ERROR_END_USER)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(MSG_GENERIC_ERROR_END_USER)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {

        return Problem.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

}

