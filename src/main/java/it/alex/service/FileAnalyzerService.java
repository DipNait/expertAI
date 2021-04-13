package it.alex.service;

import ai.expert.nlapi.exceptions.NLApiException;
import ai.expert.nlapi.v2.cloud.Analyzer;
import ai.expert.nlapi.v2.cloud.Categorizer;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import ai.expert.nlapi.v2.model.Category;
import ai.expert.nlapi.v2.model.MainLemma;
import ai.expert.nlapi.v2.model.MainPhrase;
import it.alex.entity.DocumentAnalized;
import it.alex.entity.FileInfo;
import it.alex.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class FileAnalyzerService {

    private FileUtils fileUtils ;
    private Analyzer analyzer;
    private Categorizer categorizer;

    @Autowired
    private FilesStorageService storageService;

    FileAnalyzerService (FileUtils fileUtils, Analyzer analyzer, Categorizer categorizer){
        this.fileUtils = fileUtils;
        this.analyzer = analyzer;
        this.categorizer = categorizer;
    }

    public AnalyzeResponse analyzeFile (String filename) throws NLApiException {
        AnalyzeResponse extraction ;
        String text = fileUtils.readLineByLineJava8 ("./uploads/"+filename);
        if (filename.contains(".json")) {

            JSONObject jsonObj = new JSONObject(text);
            jsonObj = jsonObj.getJSONObject("document");
            extraction = analyzer.analyze(jsonObj.getString("text"));
        }else{
            if(text.length() > 10000){
                text = text.substring(0,10000);
            }
            extraction = analyzer.analyze(text);
        }

        log.info("Analized finished :"+ filename);
        return extraction;
    }

    public CategorizeResponse categorizeFile (String filename) throws NLApiException {
        CategorizeResponse categorization ;

        String text = fileUtils.readLineByLineJava8 ("./uploads/"+filename);
        if (text.isEmpty()){
            return  null;
        }
        if (filename.contains(".json")) {
            JSONObject jsonObj = new JSONObject(text);
            jsonObj = jsonObj.getJSONObject("document");
            //Perform the IPTC classification and store it into a Response Object
            categorization = categorizer.categorize(jsonObj.getString("text"));
        }else{
            if(text.length() > 10000){
                text = text.substring(0,10000);
            }
            categorization = categorizer.categorize(text);
        }
        log.info("Categorization finished :"+ filename);
        return categorization;
    }

}
