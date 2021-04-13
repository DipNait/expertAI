package it.alex.service;

import it.alex.entity.FileInfo;
import it.alex.repository.FileInfoRepository;
import it.alex.webmodel.FileInfoWeb;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileInfoService {
    FileInfoRepository fileInfoRepository;
    public FileInfoService (FileInfoRepository fileInfoRepository){
        this.fileInfoRepository = fileInfoRepository;
    }

    public List<FileInfoWeb> retrieveFileList(){
        return fileInfoRepository.findAll().stream().map(FileInfo::convert).collect(Collectors.toList());
    }

}
