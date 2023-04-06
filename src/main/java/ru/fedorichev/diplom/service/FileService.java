package ru.fedorichev.diplom.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fedorichev.diplom.component.FileInfo;
import ru.fedorichev.diplom.config.FileUploadProperties;
import ru.fedorichev.diplom.entity.File;
import ru.fedorichev.diplom.exception.FileStorageException;
import ru.fedorichev.diplom.exception.InvalidDataException;
import ru.fedorichev.diplom.repository.FileRepository;
import ru.fedorichev.diplom.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    private final Path dirLocation;

    public FileService(FileUploadProperties fileUploadProperties, FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.dirLocation = Paths.get(fileUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Неполучилось создать директорию");
        }
    }

    public void upload(String fileName, MultipartFile file) {
        try {
            Path dFile = this.dirLocation.resolve(fileName);
            Files.copy(file.getInputStream(), dFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new InvalidDataException("Невозможно загрузить файл");
        }
    }

    public Resource load(String fileName) {
        try {
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InvalidDataException("Файл некорректен");
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("Файл не найден");
        }
    }

    public void delete(String fileName) {
        Path file = this.dirLocation.resolve(fileName).normalize();
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new FileStorageException("Проблема с удалением файла");
        }
    }

    public void change(String fileName, String newFileName) {
        try {
            Path dFile = this.dirLocation.resolve(fileName);
            Path newDFile = this.dirLocation.resolve(newFileName);

            Resource resource = new UrlResource(dFile.toUri());

            if (!resource.exists() && !resource.isReadable()) {
                throw new InvalidDataException("Файл некорректен");
            }

            Files.copy(resource.getInputStream(), newDFile, StandardCopyOption.REPLACE_EXISTING);

            delete(fileName);
        } catch (IOException e) {
            throw new FileStorageException("Ошибка перезаписи файла");
        }
    }

    public List<FileInfo> getList(Integer limit) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            System.out.println(this.dirLocation);
            Resource[] resources = resolver.getResources("file:" + this.dirLocation + "/*");

            return List
                    .of(resources)
                    .subList(0, limit < resources.length ? limit : resources.length)
                    .stream()
                    .map(
                            e -> {
                                try {
                                    return new FileInfo(
                                            e.getFilename(),
                                            (int) e.getFile().length()
                                    );
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                    ).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
