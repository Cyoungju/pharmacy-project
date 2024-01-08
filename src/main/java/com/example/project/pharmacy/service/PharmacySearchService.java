package com.example.project.pharmacy.service;

import com.example.project.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.project.pharmacy.dto.PharmacyDto;
import com.example.project.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;

    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList(){

        // 일단 redis에서 먼저 조회가 일어나고, 문제가 발생시 그때 db에서 불러올수 있게 작업 - 이전에 db내용이 redis에 들어가있어야함 PharmacyController
        // redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if(!pharmacyDtoList.isEmpty()) return pharmacyDtoList;


        //db
        return pharmacyRepositoryService.findAll()
                .stream() //자바 8이상에서 사용가능
                // .map(entity -> convertToPharmacyDto(entity)) 람다식
                .map(this::convertToPharmacyDto) //메서드 레퍼런스를 이용해서 더 간결하게 표현가능 alt+enter
                .collect(Collectors.toList());
    }
    // 전체 Pharmacy 엔티티를 조회해 와서 DTO로 변환해줌


    // entity-> Dto
    // 엔티티는 데이터베이스와 밀접하게 관련이 있음
    // DTO로 담아서 넘겨줌
    // radis와 데이터베이스를 동일한 DTO에 담아서 한 메서드에서 관리 하기 위함
    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy){
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
