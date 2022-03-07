package com.company.elixr.springbootapplicationwithdocker.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FileInfoDTO {

    private UUID id;
    private String fileName;
    private LocalDateTime timeOfUpload;
    private String statusOfFile;
}
