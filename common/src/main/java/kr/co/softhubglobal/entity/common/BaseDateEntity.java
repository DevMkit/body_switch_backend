package kr.co.softhubglobal.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseDateEntity {

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private LocalDateTime registeredDate;

    @LastModifiedDate
    @Column(name = "UPD_DT")
    private LocalDateTime updatedDate;
}
