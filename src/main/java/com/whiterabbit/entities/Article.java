package com.whiterabbit.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
//pour gerer la relation bidirectionnelle avec category (champs dbref) et eviter boucle infini
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Article {
    @Id
    private String id;
    private String titre;
    private String auteur;
    private String datePublication;
    private String dateMiseAJour;
    private String resume;
    private String contenu;
    private String img;
    private String url;
    @DBRef
    private Category category;
}
