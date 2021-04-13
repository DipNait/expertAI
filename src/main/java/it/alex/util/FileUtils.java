package it.alex.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileUtils {

    public Set<String> listFilesUsingJavaIO() {
        return Stream.of(new File("./uploads/").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (Exception e)
        {
            try(Stream<String> stream = Files.lines( Paths.get(filePath), Charset.forName("Windows-1252"))){
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }catch (Exception ex){
                log.error("Exception ex: " + e.getMessage() + "- filePath: "+filePath );
            }

        }

        return contentBuilder.toString();
    }
}
