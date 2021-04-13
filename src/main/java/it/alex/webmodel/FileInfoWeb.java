package it.alex.webmodel;

import it.alex.entity.DocumentAnalized;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class FileInfoWeb {

    private String name;
    private String url;

    private String analyzed;

    private String categorized;

    private List<DocumentAnalizedWeb> documentAnalizedWebs;
}
