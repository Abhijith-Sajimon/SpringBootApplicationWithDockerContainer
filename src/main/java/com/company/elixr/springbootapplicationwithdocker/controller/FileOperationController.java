package com.company.elixr.springbootapplicationwithdocker.controller;

import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import com.company.elixr.springbootapplicationwithdocker.service.FileOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;


@RestController
@Validated
@RequiredArgsConstructor
public class FileOperationController {

    private final FileOperationService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<SuccessResponse> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                                      @RequestParam(value = "username") @NotEmpty String userName) {
        return fileService.saveFile(file, userName);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<SuccessResponseForGetById> getFileById(@PathVariable(value = "id") String id) {
        return fileService.findFileById(id);
    }

    @GetMapping("/file/user/{userName}")
    public ResponseEntity<SuccessResponse> getFileByUserName(@PathVariable(value = "userName") String userName) {
        return fileService.findFileByUserName(userName);
    }
}
