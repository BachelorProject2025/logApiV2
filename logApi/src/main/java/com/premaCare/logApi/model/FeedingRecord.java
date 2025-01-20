package com.premaCare.logApi.model;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedingRecord {
    private String id;

    @NotNull
    private String feedingTime;

    @NotNull
    private String feedingMethod; // "probe", "bottle", "breast"

    private Double weight;
    private String date;
    private boolean pee;
    private boolean poop;
    private String comments;

}


