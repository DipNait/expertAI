package it.alex.service;

import ai.expert.nlapi.exceptions.NLApiException;
import ai.expert.nlapi.v2.cloud.Analyzer;
import ai.expert.nlapi.v2.cloud.Categorizer;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import ai.expert.nlapi.v2.model.*;
import it.alex.entity.DocumentAnalized;
import it.alex.entity.FileInfo;
import it.alex.repository.DocumentAnalizedepository;
import it.alex.repository.FileInfoRepository;
import it.alex.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DbInitializerService {
    @Autowired
    FilesStorageService storageService;

    private FileUtils fileUtils;
    private Analyzer analyzer;
    private Categorizer categorizer;
    private FileInfoRepository fileInfoRepository;
    private DocumentAnalizedepository documentAnalizedepository;


    public DbInitializerService(FileUtils fileUtils, Analyzer analyzer,
                                Categorizer categorizer,
                                FileInfoRepository fileInfoRepository,
                                DocumentAnalizedepository documentAnalizedepository) {
        this.fileUtils = fileUtils;
        this.analyzer = analyzer;
        this.categorizer = categorizer;
        this.fileInfoRepository = fileInfoRepository;
        this.documentAnalizedepository = documentAnalizedepository;
    }

    public void initDb() {
        storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();

            String url = "https://localhost:8443/files/get/" + filename;
            AnalyzeResponse analyzed = null;
            CategorizeResponse categorized = null;

            String text = fileUtils.readLineByLineJava8("./uploads/" + filename);
            if (filename.contains(".json")) {
                JSONObject jsonObj = new JSONObject(text);
                jsonObj = jsonObj.getJSONObject("document");
                try {
                    analyzed = analyzer.analyze(jsonObj.getString("text"));
                } catch (NLApiException e) {
                    log.error("NLApiException analyzer :" + e.getMessage());
                }
                try {
                    categorized = categorizer.categorize(jsonObj.getString("text"));
                } catch (NLApiException e) {
                    log.error("NLApiException categorizer:" + e.getMessage());
                }
            } else {
                if (text.length() > 10000) {
                    text = text.substring(0, 10000);
                }
                try {
                    analyzed = analyzer.analyze(text);
                } catch (NLApiException e) {
                    log.error("NLApiException analyzer :" + e.getMessage());
                }
                try {
                    categorized = categorizer.categorize(text);
                } catch (NLApiException e) {
                    log.error("NLApiException categorizer:" + e.getMessage());
                }
            }
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(filename);
            fileInfo.setUrl(url);

            Set<DocumentAnalized> set = new HashSet<>();
            //CATEGORY,PHRASE,EXCTRATION, MAIN_LEMMA, MAIN_PHRASE;
            List<MainLemma> mainLemmas = analyzed.getData().getMainLemmas();
            if (null != mainLemmas) {
                for (MainLemma lemma : mainLemmas) {
                    DocumentAnalized documentAnalized = DocumentAnalized.createMainLemma(lemma, fileInfo);
                    set.add(documentAnalized);
                }
            }
            List<MainPhrase> mainPhrases = analyzed.getData().getMainPhrases();
            if (null != mainPhrases) {
                for (MainPhrase phrase : mainPhrases) {
                    DocumentAnalized documentAnalized = DocumentAnalized.createMainPhrase(phrase, fileInfo);
                    set.add(documentAnalized);
                }
            }

            List<Category> categories = categorized.getData().getCategories();
            if (null != categories) {
                for (Category category : categories) {
                    DocumentAnalized documentAnalized = DocumentAnalized.createCategory(category, fileInfo);
                    set.add(documentAnalized);
                }
            }

            List<Phrase> phrases = analyzed.getData().getPhrases();
            if (null != phrases) {
                for (Phrase phrase : phrases) {
                    DocumentAnalized documentAnalized = DocumentAnalized.createPhrase(phrase, fileInfo);
                    set.add(documentAnalized);
                }
            }

            List<Extraction> extractions = analyzed.getData().getExtractions();
            if (null != extractions) {
                for (Extraction extraction : extractions) {
                    DocumentAnalized documentAnalized = DocumentAnalized.createExtraction(extraction, fileInfo);
                    set.add(documentAnalized);
                }
            }

            fileInfoRepository.save(fileInfo);

            documentAnalizedepository.saveAll(set);
            return fileInfo;
        }).collect(Collectors.toList());

    }

    public void loadSingleAnalizedDocument(String filename) {

        String url = "https://localhost:8443/files/get/" + filename;
        AnalyzeResponse analyzed = null;
        CategorizeResponse categorized = null;

        String text = fileUtils.readLineByLineJava8("./uploads/" + filename);
        if (filename.contains(".json")) {
            JSONObject jsonObj = new JSONObject(text);
            jsonObj = jsonObj.getJSONObject("document");
            try {
                analyzed = analyzer.analyze(jsonObj.getString("text"));
            } catch (NLApiException e) {
                log.error("NLApiException analyzer :" + e.getMessage());
            }
            try {
                categorized = categorizer.categorize(jsonObj.getString("text"));
            } catch (NLApiException e) {
                log.error("NLApiException categorizer:" + e.getMessage());
            }
        } else {
            if (text.length() > 10000) {
                text = text.substring(0, 10000);
            }
            try {
                analyzed = analyzer.analyze(text);
            } catch (NLApiException e) {
                log.error("NLApiException analyzer :" + e.getMessage());
            }
            try {
                categorized = categorizer.categorize(text);
            } catch (NLApiException e) {
                log.error("NLApiException categorizer:" + e.getMessage());
            }
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(filename);
        fileInfo.setUrl(url);

        Set<DocumentAnalized> set = new HashSet<>();
        //CATEGORY,PHRASE,EXCTRATION, MAIN_LEMMA, MAIN_PHRASE;
        List<MainLemma> mainLemmas = analyzed.getData().getMainLemmas();
        if (null != mainLemmas) {
            for (MainLemma lemma : mainLemmas) {
                DocumentAnalized documentAnalized = DocumentAnalized.createMainLemma(lemma, fileInfo);
                set.add(documentAnalized);
            }
        }
        List<MainPhrase> mainPhrases = analyzed.getData().getMainPhrases();
        if (null != mainPhrases) {
            for (MainPhrase phrase : mainPhrases) {
                DocumentAnalized documentAnalized = DocumentAnalized.createMainPhrase(phrase, fileInfo);
                set.add(documentAnalized);
            }
        }

        List<Category> categories = categorized.getData().getCategories();
        if (null != categories) {
            for (Category category : categories) {
                DocumentAnalized documentAnalized = DocumentAnalized.createCategory(category, fileInfo);
                set.add(documentAnalized);
            }
        }

        List<Phrase> phrases = analyzed.getData().getPhrases();
        if (null != phrases) {
            for (Phrase phrase : phrases) {
                DocumentAnalized documentAnalized = DocumentAnalized.createPhrase(phrase, fileInfo);
                set.add(documentAnalized);
            }
        }

        List<Extraction> extractions = analyzed.getData().getExtractions();
        if (null != extractions) {
            for (Extraction extraction : extractions) {
                DocumentAnalized documentAnalized = DocumentAnalized.createExtraction(extraction, fileInfo);
                set.add(documentAnalized);
            }
        }


        fileInfoRepository.save(fileInfo);

        documentAnalizedepository.saveAll(set);

    }

}
