package com.example.project;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


//해당 클래스를 상속 받아서 사용하게되면 생성일자나 변경일자를 자동으로 관리해줌
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate //시간이 자동으로 저장됨
    @Column(updatable = false) //업데이트 되지 말아야할 정보
    private LocalDateTime createdDate;
    //자바 8이상에서 사용하는 로컬 데이터 타입

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
