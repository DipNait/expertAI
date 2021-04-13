package it.alex.test;


import ai.expert.nlapi.security.*;
import it.alex.ConfigurationBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import ai.expert.nlapi.v2.API;
import ai.expert.nlapi.v2.cloud.Analyzer;
import ai.expert.nlapi.v2.cloud.AnalyzerConfig;
import ai.expert.nlapi.v2.message.AnalyzeResponse;
import ai.expert.nlapi.v2.model.AnalyzeDocument;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(
        classes = ConfigurationBean.class)
@TestPropertySource(
        locations = "classpath:application.yml")
public class TestToken {
    @Autowired
    Analyzer analyzer;

    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    @Test
    public void testToken () throws Exception {

        String text = readLineByLineJava8 ("./uploads/1.txt");
        AnalyzeResponse relevants = analyzer.relevants(text);
        // Output JSON representation
        System.out.println("JSON representation:");
        relevants.prettyPrint();
        // Main lemmas
        System.out.println("Main lemmas:");
        AnalyzeDocument data = relevants.getData();
        data.getMainLemmas().stream().forEach(c -> System.out.println(c.getValue()));

    }
}
