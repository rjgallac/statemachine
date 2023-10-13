package com.example.demo.model;

import com.example.demo.config.ApplicationReviewStates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue
    private long id;

    private Date date;

    @Enumerated(EnumType.STRING)
    private ApplicationReviewStates applicationReviewStates;


}
