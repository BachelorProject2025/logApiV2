package com.premaCare.logApi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class FeedingRecord {
    @NotNull
    private String id;

    @NotNull
    private String feedingTime;

    @NotNull
    private String feedingMethod; // "probe", "bottle", "breast"

    @NotNull
    private Double feedingAmount;

    @NotNull
    private Double weight;

    @NotNull
    private String date;

    @NotNull
    private Boolean pee;

    @NotNull
    private Boolean poop;

    @NotNull
    private String comments;

    @NotNull
    private String babyName;

    private List<String> babyNameKeywords; // Field for partial search
}

