package com.premaCare.logApi.model;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

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


    @Data
    public class feedingRecord {
        private String id;
        private String feedingTime;
        private String feedingMethod;
        private Double feedingAmount;
        private Double weight;
        private String date;
        private Boolean pee;
        private Boolean poop;
        private String comments;
    }

}


