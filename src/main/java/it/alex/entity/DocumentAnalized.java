package it.alex.entity;

import ai.expert.nlapi.v2.model.*;
import it.alex.enums.TypeDocument;
import it.alex.webmodel.DocumentAnalizedWeb;
import it.alex.webmodel.FileInfoWeb;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class DocumentAnalized {
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO )
    private Long id;

    @Enumerated
    private TypeDocument typeDocument;

    @Basic
    private String value;

    @Basic
    private Float frequency;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "name")
    private FileInfo fileinfo;

    public static DocumentAnalized createMainLemma(MainLemma lemma, FileInfo fileinfo){
        DocumentAnalized documentAnalized = new DocumentAnalized();
        documentAnalized.setTypeDocument(TypeDocument.MAIN_LEMMA);
        documentAnalized.setValue(lemma.getValue());
        documentAnalized.setFileinfo(fileinfo);
        return documentAnalized;
    }

    public static DocumentAnalized createExtraction(Extraction extraction, FileInfo fileinfo){
        DocumentAnalized documentAnalized = new DocumentAnalized();
        documentAnalized.setTypeDocument(TypeDocument.EXCTRATION);
        documentAnalized.setValue(extraction.getTemplate());
        documentAnalized.setFileinfo(fileinfo);
        return documentAnalized;
    }

    public static DocumentAnalized createPhrase(Phrase phrase, FileInfo fileinfo){
        DocumentAnalized documentAnalized = new DocumentAnalized();
        documentAnalized.setTypeDocument(TypeDocument.PHRASE);
        documentAnalized.setValue(phrase.getType().name());
        documentAnalized.setFileinfo(fileinfo);
        return documentAnalized;
    }

    public static DocumentAnalized createMainPhrase(MainPhrase mainPhrase, FileInfo fileinfo){
        DocumentAnalized documentAnalized = new DocumentAnalized();
        documentAnalized.setTypeDocument(TypeDocument.MAIN_PHRASE);
        documentAnalized.setValue(mainPhrase.getValue());
        documentAnalized.setFileinfo(fileinfo);
        return documentAnalized;
    }

    public static DocumentAnalized createCategory(Category category, FileInfo fileinfo){
        DocumentAnalized documentAnalized = new DocumentAnalized();
        documentAnalized.setTypeDocument(TypeDocument.CATEGORY);
        documentAnalized.setFrequency(category.getFrequency());
        documentAnalized.setFileinfo(fileinfo);
        return documentAnalized;
    }

    public DocumentAnalizedWeb convert(){
        DocumentAnalizedWeb documentAnalizedWeb = new DocumentAnalizedWeb();
        documentAnalizedWeb.setId(id);
        documentAnalizedWeb.setTypeDocument(typeDocument.name());
        documentAnalizedWeb.setValue(value);
        documentAnalizedWeb.setFrequency(frequency);
        documentAnalizedWeb.setFileinfo(fileinfo.getName());
        return documentAnalizedWeb;
    }

}
