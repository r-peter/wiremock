package com.learnwiremock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Movie {
    private Long movie_id;
    private String name;
    private String cast;
    private Integer year;
    private LocalDate release_date;
}
