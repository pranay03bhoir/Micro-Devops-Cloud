package com.pranay.easy_buy.commonservice.payload;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductSnapshot(UUID id,
        String title,
        String shortDesc,
        String longDesc,
        Double price,
        Integer discount,
        Boolean live) {

}
