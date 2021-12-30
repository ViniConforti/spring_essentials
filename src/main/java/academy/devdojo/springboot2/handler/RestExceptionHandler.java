package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Informa os controllers que eles precisam
// usar essa classe pra tratar exceções
@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /*Caso o controller tenha uma excecao do
    tipo BadRequestException, ele
    vai usar o metodo abaixo pra
    tratar ela*/
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(
            BadRequestException badRequestException) {
        return new ResponseEntity<>(BadRequestExceptionDetails.builder()
                .details(badRequestException.getMessage())
                .timeStamp(LocalDateTime.now())
                .developerMessage(badRequestException.getClass().getName())
                .title("Bad Request Exception")
                .status(HttpStatus.BAD_REQUEST.value()).build(),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrorList =
                methodArgumentNotValidException.getBindingResult()
                        .getFieldErrors();

        String fields = fieldErrorList.stream().map(FieldError::getField)
                .collect(Collectors.joining(","));
        String messages =
                fieldErrorList.stream().map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(","));


        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .details("check the fields error")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage(
                                methodArgumentNotValidException.getClass()
                                        .getName()).fields(fields)
                        .fieldsMessage(messages)
                        .title("Validation Exception, invalid fields")
                        .status(HttpStatus.BAD_REQUEST.value()).build(),
                HttpStatus.BAD_REQUEST);


    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {


        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .details(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .developerMessage(ex.getClass().getName())
                .title(ex.getCause().getMessage()).status(status.value())
                .build();


        return new ResponseEntity(exceptionDetails, headers, status);
    }
}
