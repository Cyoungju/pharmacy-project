package com.example.project.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDto {
    @JsonProperty("total_count") //제이슨으로 데이터를 전달받을때 데이터와 필드를 매핑 시켜주는 어노테이션
    private Integer totalCount;
}
