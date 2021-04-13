package it.alex.controller;

import ai.expert.nlapi.exceptions.NLApiException;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import ai.expert.nlapi.v2.model.AnalyzeDocument;
import ai.expert.nlapi.v2.model.CategorizeDocument;
import ai.expert.nlapi.v2.model.Category;
import it.alex.repository.FileInfoRepository;
import it.alex.service.FileAnalyzerService;
import it.alex.webmodel.ChartValuePie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChartController {
    private FileAnalyzerService fileAnalyzerService ;
    private FileInfoRepository fileInfoRepository ;

    public ChartController (FileAnalyzerService fileAnalyzerService, FileInfoRepository fileInfoRepository) {

        this.fileAnalyzerService = fileAnalyzerService;
        this.fileInfoRepository = fileInfoRepository;
    }

    @GetMapping("/chart")
    public String chartHome (){
        return "chart";
    }

    @GetMapping("/chart/{filename:.+}")
    public String chart (@PathVariable String filename, Model model)  throws NLApiException {
        //CategorizeResponse response = fileAnalyzerService.categorizeFile(filename);
        //String response = fileInfoRepository.findById(filename).get().convert().getCategorized();
        CategorizeResponse categorizeResponse = fileAnalyzerService.categorizeFile(filename);
        CategorizeDocument document = categorizeResponse.getData();
        List<Category> response = document.getCategories();

        AnalyzeResponse analyzeResponse = fileAnalyzerService.analyzeFile(filename);
        AnalyzeDocument documenta = analyzeResponse.getData();


        List<ChartValuePie> responsec = new ArrayList<>();
        float total = 100.00f;
        float count = 0.00f;
        for (Category category : response){
            ChartValuePie chartValuePie = new ChartValuePie();
            count+=category.getFrequency();
            chartValuePie.setY(category.getFrequency());
            chartValuePie.setName(category.getLabel());
            responsec.add(chartValuePie);
        }

        if (count<total){
            float diff = total -count ;
            ChartValuePie chartValuePie = new ChartValuePie();
            chartValuePie.setY(diff);
            chartValuePie.setName("Other");
            responsec.add(chartValuePie);
        }

       // Map<String,Object> map = response.stream().collect(Collectors.toMap(Category::getLabel, Category::getScore));

        if (null!=response){
            //model.addAttribute("data", response);
            model.addAttribute("labelScore", responsec);
        }  else{
            model.addAttribute("labelScore", "no data found");
        }
        //modelAndView.addAttribute("data", response);
        return "chart";
    }
}
