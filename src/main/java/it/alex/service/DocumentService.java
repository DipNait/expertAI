package it.alex.service;

import it.alex.entity.DocumentAnalized;
import it.alex.entity.FileInfo;
import it.alex.repository.DocumentAnalizedepository;
import it.alex.repository.FileInfoRepository;
import it.alex.webmodel.DocumentAnalizedWeb;
import it.alex.webmodel.FileInfoWeb;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    DocumentAnalizedepository documentAnalizedepository;
    public DocumentService (DocumentAnalizedepository documentAnalizedepository){
        this.documentAnalizedepository = documentAnalizedepository;
    }

    public List<DocumentAnalizedWeb> retrieveDocuments(){
        return documentAnalizedepository.findAll().stream().map(DocumentAnalized::convert).collect(Collectors.toList());
    }

    public List<DocumentAnalizedWeb> retrieveDocument(String fileName){
        return documentAnalizedepository.getAllDocumentsByFileName(fileName).stream().map(DocumentAnalized::convert).collect(Collectors.toList());
    }
}
