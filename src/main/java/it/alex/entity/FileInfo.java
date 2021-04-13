package it.alex.entity;

import it.alex.webmodel.FileInfoWeb;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class FileInfo {
    @Id
    private String name;
    @Basic
    private String url;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "fileinfo",
            orphanRemoval = true)
    private Set<DocumentAnalized> documentAnalizedList = new HashSet<>();

    public FileInfoWeb convert(){
        FileInfoWeb fileInfoWeb = new FileInfoWeb();
        fileInfoWeb.setName(name);
        fileInfoWeb.setUrl(url);
        fileInfoWeb.setDocumentAnalizedWebs(documentAnalizedList.stream().map(DocumentAnalized::convert).collect(Collectors.toList()));
        return fileInfoWeb;
    }

}
