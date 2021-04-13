package it.alex.controller;

import java.util.List;

import it.alex.entity.FileInfo;
import it.alex.repository.FileInfoRepository;
import it.alex.response.ResponseMessage;
import it.alex.service.DbInitializerService;
import it.alex.service.FileInfoService;
import it.alex.service.FilesStorageService;
import it.alex.webmodel.FileInfoWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
@CrossOrigin({"http://localhost:8080","https://localhost:8443"})
@RequestMapping("/files")
public class FilesController {
    Logger logger = LoggerFactory.getLogger(FilesController.class);

    FilesStorageService storageService;

    FileInfoService fileInfoService;

    DbInitializerService dbInitializerService;

    public FilesController (FilesStorageService storageService, FileInfoService fileInfoService, DbInitializerService dbInitializerService){

        this.storageService = storageService;
        this.fileInfoService = fileInfoService;
        this.dbInitializerService = dbInitializerService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String message = "";
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            logger.info("Called upload filename : "+file.getOriginalFilename()+ " by :"+currentPrincipalName);
            storageService.save(file);

            dbInitializerService.loadSingleAnalizedDocument(file.getOriginalFilename());

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            logger.error("Exception e,",e );
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileInfoWeb>> getListFiles() {

        List<FileInfoWeb> fileInfos = fileInfoService.retrieveFileList();
        logger.info("Called list");
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<FileInfoWeb>> getListFilesFree() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        logger.info("Called listUser by :"+currentPrincipalName);
        List<FileInfoWeb> fileInfos = fileInfoService.retrieveFileList();

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/get/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        logger.info("Called filename : "+filename+" by :"+ currentPrincipalName);
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
