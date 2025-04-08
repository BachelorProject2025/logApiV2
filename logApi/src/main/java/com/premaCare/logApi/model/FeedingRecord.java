package com.premaCare.logApi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FeedingRecord {
    @NotNull
    private String id;

    @NotNull
    private String feedingMethod; // "probe", "bottle", "breast"

    @NotNull
    private Double amount;

    @NotNull
    private Double weight;

    @NotNull
    private String date;

    @NotNull
    private Boolean pee;

    @NotNull
    private Boolean poop;

    @NotNull
    private String comment;

    @NotNull
    private Date time;



}

