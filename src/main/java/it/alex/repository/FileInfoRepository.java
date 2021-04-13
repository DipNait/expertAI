package it.alex.repository;

import it.alex.entity.FileInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileInfoRepository  extends JpaRepository<FileInfo, String> {
}
