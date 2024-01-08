package com.example.project.direction.service

import com.example.project.api.dto.DocumentDto
import com.example.project.api.service.KakaoCategorySearchService
import com.example.project.direction.repository.DirectionRepository
import com.example.project.pharmacy.dto.PharmacyDto
import com.example.project.pharmacy.service.PharmacySearchService
import spock.lang.Specification


//유닛 테스트 진행
class DirectionServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService = Mock() // 모조품 만들기

    private DirectionRepository directionRepository = Mock()

    private KakaoCategorySearchService kakaoCategorySearchService = Mock()

    private Base62Service base62Service = Mock()

    private DirectionService directionService = new DirectionService(
            pharmacySearchService, directionRepository, kakaoCategorySearchService, base62Service)

    private List<PharmacyDto> pharmacyDtoList

    def setup(){
        pharmacyDtoList = new ArrayList<>()

        //공공기관 데이터에서 가져온정보들
        pharmacyDtoList.addAll(
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName("돌곶이온누리약국")
                        .pharmacyAddress("주소1")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build(),
                PharmacyDto.builder()
                        .id(2L)
                        .pharmacyName("호수온누리약국")
                        .pharmacyAddress("주소2")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build()
        )
    }

    def "buildDirectionList - 결과값이 거리순으로 정렬이 되는지 확인"(){
        given:
        def addressName = "서울 성북구 종암로10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        pharmacySearchService.searchPharmacyDtoList() >> pharmacyDtoList
        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetPharmacyName == "호수온누리약국"
        results.get(1).targetPharmacyName == "돌곶이온누리약국"
    }


    def "buildDirectionList - 정해진 반경 10km 내에 검색이 되는지 확인"(){
        given:
        pharmacyDtoList.add(
                PharmacyDto.builder()
                        .id(3L)
                        .pharmacyName("경기약국")
                        .pharmacyAddress("주소3")
                        .latitude(37.3825107393401)
                        .longitude(127.236707811313)
                        .build())

        // 10km 가 넘는 약국

        def addressName = "서울 성북구 종암로10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        pharmacySearchService.searchPharmacyDtoList() >> pharmacyDtoList

        def results = directionService.buildDirectionList(documentDto)

        then:

        results.size() == 2
        results.get(0).targetPharmacyName == "호수온누리약국"
        results.get(1).targetPharmacyName == "돌곶이온누리약국"
    }
}
