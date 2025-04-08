package com.premaCare.logApi.model;

import lombok.Data;

@Data
public class Message {
    private String id;
    private String Senderid = "Sykepleier";
    private String message;
    private Long timestamp;
    private Boolean read; // F.eks. "nurse"
}