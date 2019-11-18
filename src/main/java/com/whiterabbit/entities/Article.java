package com.whiterabbit.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Article {
    @Id
    private String id;
    private String titre;
    private String auteur;
    private String datePublication;
    private String dateMiseAJour;
    private String resume;
    private String contenu;
    @DBRef
    private Category category;
}
