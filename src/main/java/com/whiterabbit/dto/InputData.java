package com.whiterabbit.dto;

import com.whiterabbit.entities.Category;
import com.whiterabbit.entities.CategoryOld;
import lombok.*;


@AllArgsConstructor  @NoArgsConstructor
@ToString @Data
public class InputData {
    private String id;
    private String category;
    private String subcategory;
    private String titre;
    private String auteur;
    private String datePublication;
    private String dateMiseAJour;
    private String resume;
    private String contenu;
    private String img;
    private String url;
    private CategoryOld categoryOld;
}
