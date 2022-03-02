package com.company.elixr.springbootapplicationwithdocker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Getter
@Setter
@Builder
public class FileAccessByUserNameDTO {

    private UUID id;
    private String fileName;
    private LocalDateTime timeOfUpload;
}
