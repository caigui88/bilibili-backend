package com.bilibili.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class EsIndexDTO {
    @JsonProperty("indexName")
    private String indexName;
    private Map<String,String> properties;
}
