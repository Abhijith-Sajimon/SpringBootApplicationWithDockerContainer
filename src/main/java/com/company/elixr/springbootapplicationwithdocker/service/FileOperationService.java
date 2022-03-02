package com.company.elixr.springbootapplicationwithdocker.service;

import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;


public interface FileOperationService {

    @PostConstruct
    void init();

    ResponseEntity<SuccessResponse> saveFile(MultipartFile file, String userName);

    ResponseEntity<SuccessResponseForGetById> findFileById(String id);

    ResponseEntity<SuccessResponse> findFileByUserName(String userName);
}
