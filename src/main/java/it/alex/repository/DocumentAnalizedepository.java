package it.alex.repository;

import it.alex.entity.DocumentAnalized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentAnalizedepository extends JpaRepository<DocumentAnalized, Long> {
    @Query("SELECT doc from DocumentAnalized doc where doc.fileinfo.name =?1")
    List<DocumentAnalized> getAllDocumentsByFileName(String fileName);
}
