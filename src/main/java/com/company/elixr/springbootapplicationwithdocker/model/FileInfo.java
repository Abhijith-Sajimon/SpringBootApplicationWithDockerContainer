package com.company.elixr.springbootapplicationwithdocker.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "files")
@Data
public class FileInfo {

    @MongoId
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    @NotNull
    private String fileName;
    @NotNull
    private String userName;
    private LocalDateTime timeOfUpload = LocalDateTime.now();

}
