package ru.fedorichev.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fedorichev.diplom.entity.File;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findOneByName(String fileName);
}
