package com.portOne.models;

import lombok.Data;

@Data
public class RefundResponse {

	 private String id;
	    private String status;
	    private Long amount;
	    private String currency;
	    private String paymentIntent;
}
