package com.example.project.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto { //위도 경도 정보
    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude; //위도

    @JsonProperty("x")
    private double longitude; //경도
}
