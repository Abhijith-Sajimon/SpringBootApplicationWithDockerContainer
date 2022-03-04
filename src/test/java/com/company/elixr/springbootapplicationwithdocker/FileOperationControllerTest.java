package com.company.elixr.springbootapplicationwithdocker;

import com.company.elixr.springbootapplicationwithdocker.constants.Constants;
import com.company.elixr.springbootapplicationwithdocker.controller.FileOperationController;
import com.company.elixr.springbootapplicationwithdocker.model.FileInfo;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponse;
import com.company.elixr.springbootapplicationwithdocker.responses.SuccessResponseForGetById;
import com.company.elixr.springbootapplicationwithdocker.service.FileOperationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(FileOperationController.class)
public class FileOperationControllerTest {

    FileInfo fileInfo = new FileInfo();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileOperationService fileOperationService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        fileInfo.setFileName("testFile.txt");
        fileInfo.setUserName("Nathaniel13");
    }

    @Test
    public void test_UploadFile() throws Exception {

        MockMultipartFile sampleFile = new MockMultipartFile("file", "testFile.txt",
                "text/plain",
                "This is the file content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(sampleFile)
                        .characterEncoding("UTF-8")
                        .param("username", "Alex12"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void test_UploadFile_NoFileProvided() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_GetFileById() throws Exception {

        String content = "This is the content";
        SuccessResponse data = SuccessResponse.builder().userName(fileInfo.getUserName())
                .fileName(fileInfo.getFileName()).content(content).build();

        Mockito.when(fileOperationService.findFileById(Mockito.anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(new SuccessResponseForGetById(Constants.SUCCESS, data)));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/file/id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void test_GetFileByUsername() throws Exception {

        Mockito.when(fileOperationService.findFileByUserName(Mockito.anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/file/user/username")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }
}
