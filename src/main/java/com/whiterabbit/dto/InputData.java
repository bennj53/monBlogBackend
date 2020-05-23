package com.whiterabbit.dto;

import com.whiterabbit.entities.Category;
import lombok.*;


@AllArgsConstructor  @NoArgsConstructor
@ToString @Data
public class InputData {
    private String id;
    private String titre;
    private String auteur;
    private String datePublication;
    private String dateMiseAJour;
    private String resume;
    private String contenu;
    private String img;
    private String url;
    private Category category;
}
