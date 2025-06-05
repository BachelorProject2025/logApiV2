package com.premaCare.logApi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Message {
    private String id;
    private String senderid = "Sykepleier";
    private String message;
    private Long timestamp;
    private Boolean isRead; // F.eks. "nurse"
}