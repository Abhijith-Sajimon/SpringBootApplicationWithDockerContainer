package com.company.elixr.springbootapplicationwithdocker.repository;

import com.company.elixr.springbootapplicationwithdocker.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileOperationRepository  extends MongoRepository<FileInfo, String> {

    List<FileInfo> findByUserName(String userName);
}
