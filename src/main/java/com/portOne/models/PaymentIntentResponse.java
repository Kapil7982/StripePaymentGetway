package com.portOne.models;

import lombok.Data;

@Data
public class PaymentIntentResponse {

	private String id;
    private Long amount;
    private String currency;
    private String status;

}
