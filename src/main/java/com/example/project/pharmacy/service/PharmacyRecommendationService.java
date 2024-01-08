package com.example.project.pharmacy.service;

import com.example.project.api.dto.DocumentDto;
import com.example.project.api.dto.KakaoApiResponseDto;
import com.example.project.api.service.KakaoAddressSearchService;
import com.example.project.direction.dto.OutputDto;
import com.example.project.direction.entity.Direction;
import com.example.project.direction.service.Base62Service;
import com.example.project.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;
    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";


    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;


    // 길안내 메서드
    public List<OutputDto> recommendPharmacyList(String address){

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        // 주소검색이 안될경우 - 재처리 해놓은 null / 주소와 맵핑되는 위도 경도가 안나올수 있음
        // Validation 체크
        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtos())){
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address : {}", address);
            return Collections.emptyList();
        }

        // 첫번째값만 가져옴 - 가까운 약국을 찾게됨
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtos().get(0); 

        // 거리 계산 알고리즘을 이용해서
        List<Direction> directions = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        // 데이터 베이스에 결과값 저장
        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){




        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude()+","+ direction.getInputLongitude())
                .distance(String.format("%.2f km", direction.getDistance())) //소수점 두번째 자리수 까지만 노출
                .build();
    }

}
