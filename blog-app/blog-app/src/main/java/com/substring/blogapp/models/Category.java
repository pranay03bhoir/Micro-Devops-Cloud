package com.substring.blogapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity // These annotations are the metadata of the class when you write them above a class.
// And if you write them above a method, then these become metatdat of the method.
// These annotations carry extra information about the class.
// The Entity annotation is used to tell the JPA that this class is a JPA entity.
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category") // One category can have multiple categories.
    private ArrayList<Article> articles = new ArrayList<>();

}