package it.alex.webmodel;

import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class DocumentAnalizedWeb {

    private Long id;

    private String typeDocument;

    private String value;

    private Float frequency;

    private String fileinfo;
}
