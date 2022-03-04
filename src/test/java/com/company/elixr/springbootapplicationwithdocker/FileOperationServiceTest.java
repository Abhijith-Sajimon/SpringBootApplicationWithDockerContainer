package com.company.elixr.springbootapplicationwithdocker;

import com.company.elixr.springbootapplicationwithdocker.constants.Constants;
import com.company.elixr.springbootapplicationwithdocker.model.FileInfo;
import com.company.elixr.springbootapplicationwithdocker.repository.FileOperationRepository;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import com.company.elixr.springbootapplicationwithdocker.service.FileOperationServiceImpl;
import org.junit.Assert;
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
public class FileOperationServiceTest {

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
        Assert.assertNotNull(created);
        Assert.assertEquals(HttpStatus.OK, created.getStatusCode());
        Assert.assertEquals(Constants.STATUS, created.getBody().getStatus());
        Assert.assertEquals(fileInfo.getId().getClass().getName(), created.getBody().getId().getClass().getName());
    }

    @Test
    public void getFileById() {

        UUID id = UUID.fromString("ebadf9b2-ea84-4683-bb32-a5bb4952e265");
        Mockito.when(fileOperationRepository.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(fileInfo));
        ResponseEntity<SuccessResponseForGetById> found = fileOperationService
                .findFileById(String.valueOf(id));
        Assert.assertNotNull(found);
        Assert.assertEquals(HttpStatus.OK, found.getStatusCode());
        Assert.assertEquals(2, found.getBody().getClass().getDeclaredFields().length);
        Assert.assertEquals(Constants.SUCCESS, found.getBody().getSuccess());
        Assert.assertEquals(fileInfo.getUserName(), found.getBody().getData().getUserName());
        Assert.assertEquals(fileInfo.getFileName(), found.getBody().getData().getFileName());
    }

    @Test
    public void getFileByUsername() {

        Mockito.when(fileOperationRepository.findByUserName(Mockito.anyString()))
                .thenReturn(fileInfoList);
        ResponseEntity<SuccessResponse> found = fileOperationService.findFileByUserName(fileInfo.getUserName());
        Assert.assertNotNull(found);
        Assert.assertEquals(HttpStatus.OK, found.getStatusCode());
        Assert.assertEquals(Constants.STATUS, found.getBody().getStatus());
        Assert.assertEquals(fileInfo.getUserName(), found.getBody().getUserName());
        Assert.assertEquals(fileInfoList.get(0).getFileName(), found.getBody().getFiles().get(0).getFileName());
    }
}
