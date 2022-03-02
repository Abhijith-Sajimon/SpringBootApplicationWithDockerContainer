package com.company.elixr.springbootapplicationwithdocker.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

    private String success;
    private String message;
}
