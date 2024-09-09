package com.workeache.precionline.api.demo.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TOKENS")
public class TokenEntity {
    @Id
    private Long number;

    private String token;
}
