package it.alex.controller;

import ai.expert.nlapi.exceptions.NLApiException;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import ai.expert.nlapi.v2.model.AnalyzeDocument;
import ai.expert.nlapi.v2.model.CategorizeDocument;
import ai.expert.nlapi.v2.model.Category;
import it.alex.repository.FileInfoRepository;
import it.alex.service.DocumentService;
import it.alex.service.FileAnalyzerService;
import it.alex.webmodel.ChartValuePie;
import it.alex.webmodel.DocumentAnalizedWeb;
import it.alex.webmodel.FileInfoWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class DocumentController {
    private DocumentService documentService ;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentAnalizedWeb>> getDocumentsList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info("Called getDocumentsList by :"+currentPrincipalName);
        List<DocumentAnalizedWeb> documentAnalizedWebs = documentService.retrieveDocuments();

        return ResponseEntity.status(HttpStatus.OK).body(documentAnalizedWebs);
    }

    @GetMapping("/documents/{filename:.+}")
    public ResponseEntity<List<DocumentAnalizedWeb>> documentByFileName (@PathVariable String filename, Model model)  throws NLApiException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info("Called getDocumentsList by :"+currentPrincipalName);
        List<DocumentAnalizedWeb> documentAnalizedWebs = documentService.retrieveDocument(filename);

        return ResponseEntity.status(HttpStatus.OK).body(documentAnalizedWebs);
    }
}
