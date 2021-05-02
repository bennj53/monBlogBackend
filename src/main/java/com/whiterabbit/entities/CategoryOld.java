package com.whiterabbit.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

/*@Document
@Data
@AllArgsConstructor @NoArgsConstructor
//pour gerer la relation bidirectionnelle avec article(champs dbref) et eviter boucle infini
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class CategoryOld {
/*    @Id
    private String id;
    private String name;
    @DBRef
    //@JsonManagedReference
    private Collection<Article> articles = new ArrayList<>();

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }*/
}
