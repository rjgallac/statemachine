package com.example.demo.model;

import com.example.demo.config.ApplicationReviewStates;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplicationDto {
    private long id;

    private Date date;

    private ApplicationReviewStates applicationReviewStates;

    private List<String> events;
}
