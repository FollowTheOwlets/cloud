package ru.fedorichev.diplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fedorichev.diplom.component.NewNameRequest;
import ru.fedorichev.diplom.exception.InvalidDataException;
import ru.fedorichev.diplom.service.FileService;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<String> upload(@RequestParam(name = "filename") String fileName, @RequestParam("file") MultipartFile fileBody) {
        fileService.upload(fileName,fileBody);
        return ResponseEntity.ok("");
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam(name = "filename") String fileName) {
        fileService.delete(fileName);
        return ResponseEntity.ok("");
    }

    @PutMapping()
    public ResponseEntity<String> change(@RequestParam(name = "filename") String fileName, @RequestBody NewNameRequest newNameRequest) {
        if(newNameRequest.getName() == null){
            throw new InvalidDataException("Некорректное тело запроса");
        }
        fileService.change(fileName, newNameRequest.getName());
        return ResponseEntity.ok("");
    }

    @GetMapping()
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "filename") String filename) {

        Resource resource = fileService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
