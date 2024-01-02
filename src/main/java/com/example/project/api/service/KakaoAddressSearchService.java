package com.example.project.api.service;

import com.example.project.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor //생성자 주입
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Retryable(
            value = {RuntimeException.class}, //재처리 하고 싶은 클래스 지정
            maxAttempts = 2, // 최대 시도할 횟스
            backoff = @Backoff(delay = 2000) // 딜레이 주고싶은 시간
    )
    public KakaoApiResponseDto requestAddressSearch(String address){

        if(ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAk " +kakaoRestApiKey);  //공백 넣어야함
        HttpEntity httpEntity = new HttpEntity<>(headers);

        // kakao api 호출 - exchange Get
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();

    }

    // 재처리가 모두 실패 했을경우 실행될 리커버 메소드 - 원래 메소드의 리턴타입을 맞춰 줘야함 (requestAddressSearch)
    @Recover
    public KakaoApiResponseDto response(RuntimeException e, String address){
        log.error("All the retries failed. address: {}, error: {}", address , e.getMessage());
        return null;
    }


}


