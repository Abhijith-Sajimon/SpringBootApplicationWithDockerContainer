package com.company.elixr.springbootapplicationwithdocker.service;

import com.company.elixr.springbootapplicationwithdocker.constants.Constants;
import com.company.elixr.springbootapplicationwithdocker.exception.BadRequestException;
import com.company.elixr.springbootapplicationwithdocker.exception.FileStorageException;
import com.company.elixr.springbootapplicationwithdocker.exception.NotFoundException;
import com.company.elixr.springbootapplicationwithdocker.model.FileInfo;
import com.company.elixr.springbootapplicationwithdocker.model.FileInfoDTO;
import com.company.elixr.springbootapplicationwithdocker.repository.FileOperationRepository;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileOperationServiceImpl implements FileOperationService {

    private final FileOperationRepository fileOperationRepository;

    @Value(Constants.LOCAL_STORAGE_FOLDER_PATH)
    private String dirLocation;

    @Override
    public void init() {
        try {
            Files.createDirectories(Path.of(dirLocation));
        } catch (Exception ex) {
            throw new FileStorageException(Constants.ERROR_CREATING_UPLOAD_DIRECTORY);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> saveFile(MultipartFile file, String userName) {
        if (Objects.equals(file.getContentType(), Constants.REQUIRED_FILE_TYPE)) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(fileName);
                fileInfo.setUserName(userName);
                UUID id = fileOperationRepository.save(fileInfo).getId();
                Path resolvedPath = Path.of(dirLocation).resolve(String.valueOf(id));
                Files.copy(file.getInputStream(), resolvedPath, StandardCopyOption.REPLACE_EXISTING);
                fileOperationRepository.save(fileInfo);
                return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder()
                        .status(Constants.STATUS).id(id).build());
            } catch (IOException | NoSuchElementException e) {
                throw new FileStorageException(Constants.ERROR_UPLOADING_FILE);
            }
        } else {
            throw new BadRequestException(Constants.ERROR_BAD_REQUEST_FILE_NOT_PRESENT_OR_INVALID_FILE_TYPE);
        }
    }

    @Override
    public ResponseEntity<SuccessResponseForGetById> findFileById(String id) {

        if ((id.matches(Constants.UUID_FORMAT))) {
            UUID uuid = UUID.fromString(id);
            FileInfo targetFileInfo = fileOperationRepository.findById(uuid).orElseThrow(()
                    -> new NotFoundException(Constants.ERROR_NOT_FOUND));
            Path targetPath = Path.of(dirLocation).resolve(String.valueOf(uuid)).normalize();
            try {
                Resource resource = new UrlResource(targetPath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new SuccessResponseForGetById(Constants.SUCCESS,
                                    SuccessResponse.builder().userName(targetFileInfo.getUserName())
                                            .fileName(targetFileInfo
                                                    .getFileName()).timeOfUpload(targetFileInfo.getTimeOfUpload())
                                            .content(stringBuilder.toString()).build()));
                } else {
                    throw new NotFoundException(Constants.ERROR_NOT_FOUND);
                }
            } catch (IOException | NoSuchElementException e) {
                throw new NotFoundException(Constants.ERROR_NOT_FOUND);
            }
        } else {
            throw new BadRequestException(Constants.ERROR_BAD_REQUEST_INVALID_ID_FORMAT);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> findFileByUserName(String userName) {

        List<FileInfo> targetFileDetails = fileOperationRepository.findByUserName(userName);
        if (targetFileDetails.isEmpty()) {
            throw new NotFoundException(Constants.ERROR_NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().status(Constants.STATUS)
                    .userName(userName).files(targetFileDetails.stream().map(this::convertDataIntoDTO)
                            .collect(Collectors.toList())).build());
        }
    }

    private FileInfoDTO convertDataIntoDTO(FileInfo fileInfo) {
        return FileInfoDTO.builder().id(fileInfo.getId()).fileName(fileInfo.getFileName())
                .timeOfUpload(fileInfo.getTimeOfUpload()).build();
    }
}
