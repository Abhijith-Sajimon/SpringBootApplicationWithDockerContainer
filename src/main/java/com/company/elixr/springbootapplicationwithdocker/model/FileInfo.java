package com.company.elixr.springbootapplicationwithdocker.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "files")
@Data
@Component
public class FileInfo {

    @MongoId(FieldType.STRING)
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    @NotNull
    private String fileName;
    @NotNull
    private String userName;
    private LocalDateTime timeOfUpload = LocalDateTime.now();
}
