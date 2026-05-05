package com.substring.blogapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortDesc;
    // Here the blog content can be huge, but by default the DB uses varchar, which uses a default size of 255,
    // So we can use the column annotation to specify the size of the variable in the DB
//    @Column(name = "article_content", nullable = false, length = 23565),
    // But we have a better solution of increasing the size of the variable based on the content.
    // by using the -
    @Lob // This annotation can increase the variable size based on the user's content szie.
    private String content;
    private Integer readingMinutes;
    private Boolean paid;
    @Enumerated(EnumType.STRING) // This annotation tells the database to save the ENUMs in String format.
    private Status status;
    private Double rating;
    private Double price;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;
}
