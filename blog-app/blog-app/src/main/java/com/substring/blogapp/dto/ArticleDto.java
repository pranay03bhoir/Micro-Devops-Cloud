package com.substring.blogapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.substring.blogapp.models.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String shortDesc;
    private String content;
    private Integer readingMinutes;
    private Boolean paid;
    private Status status;
    private Double rating;
    private Double price;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    // update only:
    /**
     * The @JsonProperty annotation with access = JsonProperty.Access.WRITE_ONLY is used to indicate that the field should not be serialized (written out) when the object is converted to JSON.
     * Only the fields that are marked with this annotation will be ignored during serialization.
     * This is useful when a field is used for internal logic and should not be exposed to the client.
     * What it means is that the user doesn't need to know the categoryId in the response;
     * thus we use this annotation so that it is only used for internal logic of the application.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long categoryId;

}
