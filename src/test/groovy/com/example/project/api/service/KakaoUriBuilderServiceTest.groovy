package com.example.project.api.service

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import java.nio.charset.StandardCharsets


class KakaoUriBuilderServiceTest extends Specification {
    private KakaoUriBuilderService kakaoUriBuilderService;

    def setup(){ // 모든 메서드 전에 실행됨
        kakaoUriBuilderService = new KakaoUriBuilderService();
    }

    //유닛 테스트
    def "buildUriByAddressSearch - 한글 파라미터의 경우 정상으로 인코딩"(){
        given:
        String  address = "서울 성북구";
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)

        //디코딩해서 결과값이 잘 나오는지 확인해봐야함 -Decoder
        def decodeResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodeResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"

    }

}
