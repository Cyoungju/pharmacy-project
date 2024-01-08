package com.example.project.pharmacy.cache;


import com.example.project.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Slf4j
@Service
public class PharmacyRedisTemplateService {
    private static final String CACHE_KEY = "PHARMACY"; //키값

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; // 빈으로 등록후 싱글톤으로 사용

    private HashOperations<String, String, String> hashOperations;
                          // key,subKey(PK), value

    @PostConstruct
    public void init(){
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(PharmacyDto pharmacyDto) {
        if(Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.getId().toString(),
                    serializePharmacyDto(pharmacyDto));
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<PharmacyDto> findAll() {

        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PharmacyDto pharmacyDto = deserializePharmacyDto(value);
                list.add(pharmacyDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete]: {} ", id);
    }


    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        //약국dto를 받아서 json형태의 타입으로 변환
        return objectMapper.writeValueAsString(pharmacyDto);
    }

    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
        //반대로 json형태의 타입을 약국dto로 담아서전달
        return objectMapper.readValue(value, PharmacyDto.class);
    }
}
