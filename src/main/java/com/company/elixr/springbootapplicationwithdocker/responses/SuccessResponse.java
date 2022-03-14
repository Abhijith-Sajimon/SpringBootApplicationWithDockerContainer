package com.company.elixr.springbootapplicationwithdocker.responses;

import com.company.elixr.springbootapplicationwithdocker.model.FileInfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {

    private String status;
    private UUID id;
    private String userName;
    private String fileName;
    private LocalDateTime timeOfUpload;
    private String content;
    private List<FileInfoDTO> files;
}
