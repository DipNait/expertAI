package it.alex.controller;

import ai.expert.nlapi.exceptions.NLApiException;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.message.CategorizeResponse;
import it.alex.service.FileAnalyzerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin({"http://localhost:8080","https://localhost:8443"})
@RequestMapping("/api2")
public class ExpertController {
    private FileAnalyzerService fileAnalyzerService ;

    public ExpertController (FileAnalyzerService fileAnalyzerService) {
        this.fileAnalyzerService = fileAnalyzerService;
    }

    @GetMapping("/analyzeFull/{filename:.+}")
    @ResponseBody
    public ResponseEntity<AnalyzeResponse> analyzeFile(@PathVariable String filename) throws NLApiException {
        AnalyzeResponse response = fileAnalyzerService.analyzeFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/categorize/{filename:.+}")
    @ResponseBody
    public ResponseEntity<CategorizeResponse> categorizeFile(@PathVariable String filename) throws NLApiException {
        CategorizeResponse response = fileAnalyzerService.categorizeFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

   // @RequestMapping(value="/categorizeChart/{filename:.+}",method = RequestMethod.GET)
   // @GetMapping("/categorizeChart/{filename:.+}")
    public String chartChategorize (@PathVariable String filename, Model model) throws NLApiException {
        CategorizeResponse response = fileAnalyzerService.categorizeFile(filename);
        model.addAttribute("message", "next page");
        model.addAttribute("data", response);
        return "chart";
    }

    @RequestMapping(value = "/categorizeChart/{filename:.+}", method = RequestMethod.GET)
    public String addData(@PathVariable String filename, Model model) throws NLApiException {
        //your code
        CategorizeResponse response = fileAnalyzerService.categorizeFile(filename);
        model.addAttribute("message", "next page");
        model.addAttribute("data", response);
        return "redirect:/chart";
    }

}
