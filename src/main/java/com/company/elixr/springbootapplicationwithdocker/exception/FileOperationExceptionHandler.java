package com.company.elixr.springbootapplicationwithdocker.exception;

import com.company.elixr.springbootapplicationwithdocker.constants.Constants;
import com.company.elixr.springbootapplicationwithdocker.responses.ErrorResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@NoArgsConstructor
public class FileOperationExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(NotFoundException exception) {

        ErrorResponse err = buildResponse(exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Object> handleFileStorageException(FileStorageException exception) {

        ErrorResponse err = buildResponse(exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(err);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {

        ErrorResponse err = buildResponse(exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        ErrorResponse err = buildResponse(Constants.ERROR_BAD_REQUEST_SERVLET_REQUEST_PART_FILE_MISSING);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ErrorResponse err = buildResponse(Constants.ERROR_BAD_REQUEST_SERVLET_REQUEST_PART_USERNAME_MISSING);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorResponse err = buildResponse(Constants.ERROR_BAD_REQUEST_REQUEST_PARAM_USERNAME_MISSING);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        ErrorResponse err = buildResponse(Constants.ERROR_BAD_REQUEST_FILE_NOT_PRESENT_OR_INVALID_FILE_TYPE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception exception) {
        ErrorResponse err = buildResponse(Constants.ERROR_UNEXPECTED_TYPE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    private ErrorResponse buildResponse(String message) {
        return ErrorResponse.builder().success(Constants.FAILURE).message(message).build();
    }
}
