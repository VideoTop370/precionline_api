package com.workeache.precionline.api.demo.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="data")
@EntityListeners(AuditingEntityListener.class)
public class DataApiReeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "LONGTEXT")
    private String data;

    private LocalDate dateFile;

    //@CreatedBy
    //protected String createdAuditBy;

    @CreatedDate
    protected LocalDateTime createAuditDate;

    //@LastModifiedBy
    //protected String lastModifiedAuditBy;

    @LastModifiedDate
    LocalDateTime lastModifiedAuditDate;

}
