package com.company.elixr.springbootapplicationwithdocker.responses;

import lombok.Data;

@Data
public class SuccessResponseForGetById {

    private String success;
    private SuccessResponse data;

    public SuccessResponseForGetById(String success, SuccessResponse data) {
        this.success = success;
        this.data = data;
    }
}
