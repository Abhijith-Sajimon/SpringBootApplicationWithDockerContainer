package com.company.elixr.springbootapplicationwithdocker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileStorageAndAccessException extends RuntimeException {

    private String message;
}
