package ru.fedorichev.diplom.service;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.fedorichev.diplom.config.FileUploadProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    private FileService fileService;

    private String filename;
    private MultipartFile file;

    @BeforeEach
    public void initEach() {
        fileService = new FileService(FileUploadProperties.builder().location("./test_uploads").build());
        fileService.init();
        filename = "test_file";

        file = Mockito.mock(MultipartFile.class);
        try {
            Mockito.lenient().when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test_stream".getBytes()));
        } catch (IOException e) {
            System.out.println("file.getInputStream() not supported mock");
        }
    }

    @DisplayName("JUnit test for upload method")
    @Test
    public void testUpload() {
        Assertions.assertTrue(fileService.upload(filename, file));
        fileService.delete(filename);
    }

    @DisplayName("JUnit test for delete method")
    @Test
    public void testDelete() {
        fileService.upload(filename, file);
        Assertions.assertTrue(fileService.delete(filename));
    }

    @DisplayName("JUnit test for change method")
    @Test
    public void testChange() {
        fileService.upload(filename, file);
        Assertions.assertTrue(fileService.change(filename, "new_name"));
        fileService.delete("new_name");
    }

    @DisplayName("JUnit test for getList method")
    @Test
    public void testGetList() {
        fileService.upload(filename, file);
        Assertions.assertEquals(fileService.getList(1).get(0).getFileName(), filename);
        fileService.delete(filename);
    }

    @DisplayName("JUnit test for load method")
    @Test
    public void testLoad() {
        fileService.upload(filename, file);
        Assertions.assertEquals(fileService.load(filename).getFilename(), filename);
        fileService.delete(filename);
    }
}
