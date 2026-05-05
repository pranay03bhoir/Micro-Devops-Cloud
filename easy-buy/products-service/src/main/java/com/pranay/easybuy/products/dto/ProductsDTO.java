package com.pranay.easybuy.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsDTO {
    private UUID id;
    private String title;
    private String shortDesc;
    private String longDesc;
    private Double price;
    private Integer discount;
    private Boolean live;
    private List<String> productImages;
    private List<CategoryDTO> categories;
    private List<ReviewDTO> reviews;
}
