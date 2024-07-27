package com.workeache.precionline.api.demo.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="data")
public class DataApiRee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "LONGTEXT")
    private String data;

    private LocalDate dateFile;

}
