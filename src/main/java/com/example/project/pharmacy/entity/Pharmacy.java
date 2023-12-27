package com.example.project.pharmacy.entity;

import com.example.project.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="pharmacy")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy extends BaseTimeEntity {
// 생성일자나 변경일자를 자동으로 관리해 주기 위해서 추상클래스인 BaseTimeEntity을 상속받아 사용함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //pk값을 데이터 베이스가 알아서 만들어줌
    private Long id;

    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude; //위도
    private double longitude; //경도


    public void changePharmacyAddress(String address){
        this.pharmacyAddress = address;
    }

}




