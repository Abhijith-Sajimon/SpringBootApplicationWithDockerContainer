package com.company.elixr.springbootapplicationwithdocker.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponseForGetById {

    private String success;
    private SuccessResponse data;
}
