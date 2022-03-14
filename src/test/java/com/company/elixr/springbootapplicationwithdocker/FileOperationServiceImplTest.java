package com.company.elixr.springbootapplicationwithdocker;

import com.company.elixr.springbootapplicationwithdocker.constants.Constants;
import com.company.elixr.springbootapplicationwithdocker.exception.BadRequestException;
import com.company.elixr.springbootapplicationwithdocker.exception.NotFoundException;
import com.company.elixr.springbootapplicationwithdocker.model.FileInfo;
import com.company.elixr.springbootapplicationwithdocker.repository.FileOperationRepository;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import com.company.elixr.springbootapplicationwithdocker.service.FileOperationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(FileOperationServiceImpl.class)
public class FileOperationServiceImplTest {

    List<FileInfo> fileInfoList = new ArrayList<>();
    @MockBean
    private FileOperationRepository fileOperationRepository;
    @Autowired
    private FileOperationServiceImpl fileOperationService;
    private final FileInfo fileInfo = new FileInfo();

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        fileInfo.setId(UUID.randomUUID());
        fileInfo.setFileName("testFile.txt");
        fileInfo.setUserName("Nathan122");
        fileInfo.setTimeOfUpload(LocalDateTime.now());

        fileInfoList.add(fileInfo);
    }

    @Test
    void test_SaveFile() {

        MockMultipartFile sampleFile = new MockMultipartFile("file", "testFile.txt",
                "text/plain",
                "This is the file content".getBytes());
        Mockito.when(fileOperationRepository.save(Mockito.any(FileInfo.class))).thenReturn(fileInfo);
        ResponseEntity<SuccessResponse> created = fileOperationService.saveFile(sampleFile, "Natasha11");
        Assertions.assertNotNull(created);
        Assertions.assertEquals(HttpStatus.OK, created.getStatusCode());
        Assertions.assertEquals(Constants.STATUS, created.getBody().getStatus());
        Assertions.assertEquals(fileInfo.getId().getClass().getName(), created.getBody().getId().getClass().getName());
    }

    @Test
    void test_SaveFile_InvalidFileType() {

        MockMultipartFile sampleFile = new MockMultipartFile("file", "testFile.json",
                "application/json",
                "This is the file content".getBytes());
        Mockito.when(fileOperationRepository.save(Mockito.any(FileInfo.class)))
                .thenThrow(new BadRequestException(Constants.ERROR_BAD_REQUEST_FILE_NOT_PRESENT_OR_INVALID_FILE_TYPE));
        try
        {
            fileOperationService.saveFile(sampleFile, "Natasha11");
            Assertions.fail("Since no exception was thrown");
        } catch (BadRequestException exception) {
            Assertions.assertNotNull(exception);
            Assertions.assertEquals((BadRequestException.class).getName(),
                    exception.getClass().getName());
            Assertions.assertEquals(Constants.ERROR_BAD_REQUEST_FILE_NOT_PRESENT_OR_INVALID_FILE_TYPE,
                    exception.getMessage());
        }
    }

    @Test
    public void test_getFileById() {

        UUID id = UUID.fromString("07ecb4bb-b804-46fc-a5c6-fca0ba2d7733");
        Mockito.when(fileOperationRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(fileInfo));
        ResponseEntity<SuccessResponseForGetById> found = fileOperationService
                .findFileById(String.valueOf(id));
        Assertions.assertNotNull(found);
        Assertions.assertEquals(HttpStatus.OK, found.getStatusCode());
        Assertions.assertEquals(2, found.getBody().getClass().getDeclaredFields().length);
        Assertions.assertEquals(Constants.SUCCESS, found.getBody().getSuccess());
        Assertions.assertEquals(fileInfo.getUserName(), found.getBody().getData().getUserName());
        Assertions.assertEquals(fileInfo.getFileName(), found.getBody().getData().getFileName());
    }

    @Test
    public void test_getFileById_IdNotInUUIDFormat() {

        String id = "ebadf9b2ea844683";
        Mockito.when(fileOperationRepository.findById(Mockito.any(UUID.class)))
                .thenThrow(new BadRequestException(Constants.ERROR_BAD_REQUEST_INVALID_ID_FORMAT));
        try {
            fileOperationService.findFileById(id);
            Assertions.fail("Since no exception was thrown");
        } catch (BadRequestException exception) {
            Assertions.assertNotNull(exception);
            Assertions.assertEquals((BadRequestException.class).getName(),
                    exception.getClass().getName());
            Assertions.assertEquals(Constants.ERROR_BAD_REQUEST_INVALID_ID_FORMAT,
                    exception.getMessage());
        }
    }

    @Test
    public void test_getFileById_NoRecordFound() {

        UUID id = UUID.fromString("ebadf9b2-ea84-4683-bb32-a5bb4952e265");
        Mockito.when(fileOperationRepository.findById(Mockito.any(UUID.class)))
                .thenThrow(new NotFoundException(Constants.ERROR_NOT_FOUND));
        try {
            fileOperationService.findFileById(String.valueOf(id));
            Assertions.fail("Since no exception was thrown");
        } catch (NotFoundException exception) {
            Assertions.assertNotNull(exception);
            Assertions.assertEquals((NotFoundException.class).getName(),
                    exception.getClass().getName());
            Assertions.assertEquals(Constants.ERROR_NOT_FOUND,
                    exception.getMessage());
        }
    }

    @Test
    public void test_getFileByUsername() {

        Mockito.when(fileOperationRepository.findByUserName(Mockito.anyString()))
                .thenReturn(fileInfoList);
        ResponseEntity<SuccessResponse> found = fileOperationService.findFileByUserName(fileInfo.getUserName());
        Assertions.assertNotNull(found);
        Assertions.assertEquals(HttpStatus.OK, found.getStatusCode());
        Assertions.assertEquals(Constants.STATUS, found.getBody().getStatus());
        Assertions.assertEquals(fileInfo.getUserName(), found.getBody().getUserName());
        Assertions.assertEquals(fileInfoList.get(0).getFileName(), found.getBody().getFiles().get(0).getFileName());
    }

    @Test
    public void test_getFileByUserName_NoRecordFound() {

        Mockito.when(fileOperationRepository.findByUserName(Mockito.anyString()))
                .thenThrow(new NotFoundException(Constants.ERROR_NOT_FOUND));
        try {
            fileOperationService.findFileByUserName(fileInfo.getUserName());
            Assertions.fail("Since no exception was thrown");
        } catch (NotFoundException exception) {
            Assertions.assertNotNull(exception);
            Assertions.assertEquals((NotFoundException.class).getName(),
                    exception.getClass().getName());
            Assertions.assertEquals(Constants.ERROR_NOT_FOUND,
                    exception.getMessage());
        }
    }
}
