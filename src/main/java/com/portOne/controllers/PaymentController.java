package com.portOne.controllers;

import com.portOne.models.PaymentIntentEntity;
import com.portOne.models.PaymentIntentResponse;
import com.portOne.models.RefundResponse;
import com.portOne.repositories.PaymentIntentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCaptureParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    @Autowired
    private PaymentIntentRepository paymentIntentRepository;

    @PostMapping("/create_intent")
    public ResponseEntity<PaymentIntentResponse> createIntent() {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(2000L)
                    .setCurrency("usd")
                    .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            PaymentIntentEntity paymentIntentEntity = new PaymentIntentEntity();
            paymentIntentEntity.setPaymentIntentId(paymentIntent.getId());
            paymentIntentEntity.setAmount(paymentIntent.getAmount());
            paymentIntentEntity.setCurrency(paymentIntent.getCurrency());
            paymentIntentEntity.setStatus(paymentIntent.getStatus());

            paymentIntentRepository.save(paymentIntentEntity);

            PaymentIntentResponse response = new PaymentIntentResponse();
            response.setId(paymentIntent.getId());
            response.setAmount(paymentIntent.getAmount());
            response.setCurrency(paymentIntent.getCurrency());
            response.setStatus(paymentIntent.getStatus());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    
    @PostMapping("/confirm_intent/{id}")
    public ResponseEntity<PaymentIntentResponse> confirmIntent(@PathVariable String id) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
            PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod("pm_card_visa")
                    .setReturnUrl("https://www.example.com")
                    .build();

            PaymentIntent confirmedIntent = paymentIntent.confirm(params);

            // Update the status in the database
            PaymentIntentEntity paymentIntentEntity = paymentIntentRepository.findByPaymentIntentId(id);
            if (paymentIntentEntity != null) {
                paymentIntentEntity.setStatus(confirmedIntent.getStatus());
                paymentIntentRepository.save(paymentIntentEntity);
            }

            PaymentIntentResponse response = new PaymentIntentResponse();
            response.setId(confirmedIntent.getId());
            response.setAmount(confirmedIntent.getAmount());
            response.setCurrency(confirmedIntent.getCurrency());
            response.setStatus(confirmedIntent.getStatus());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    

    @PostMapping("/capture_intent/{id}")
    public ResponseEntity<String> captureIntent(@PathVariable String id) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

            // Check the status of the PaymentIntent
            if (!"requires_capture".equals(paymentIntent.getStatus())) {
                return ResponseEntity.status(400).body("PaymentIntent must be in requires_capture status to be captured. Current status: " + paymentIntent.getStatus());
            }

            PaymentIntentCaptureParams params = PaymentIntentCaptureParams.builder().build();
            PaymentIntent capturedIntent = paymentIntent.capture(params);

            // Update the status in the database
            PaymentIntentEntity paymentIntentEntity = paymentIntentRepository.findByPaymentIntentId(id);
            if (paymentIntentEntity != null) {
                paymentIntentEntity.setStatus(capturedIntent.getStatus());
                paymentIntentRepository.save(paymentIntentEntity);
            }

            return ResponseEntity.ok("PaymentIntent captured successfully. ID: " + capturedIntent.getId());
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Stripe exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/create_refund/{id}")
    public ResponseEntity<RefundResponse> createRefund(@PathVariable String id) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
            Map<String, Object> params = new HashMap<>();
            params.put("payment_intent", paymentIntent.getId());
            params.put("amount", paymentIntent.getAmount());

            // Create the refund
            Refund refund = Refund.create(params);

            // Update the status in the database
            PaymentIntentEntity paymentIntentEntity = paymentIntentRepository.findByPaymentIntentId(id);
            if (paymentIntentEntity != null) {
                paymentIntentEntity.setStatus("refunded");
                paymentIntentRepository.save(paymentIntentEntity);
            }

            // Create a response object
            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setId(refund.getId());
            refundResponse.setStatus(refund.getStatus());
            refundResponse.setAmount(refund.getAmount());
            refundResponse.setCurrency(refund.getCurrency());
            refundResponse.setPaymentIntent(refund.getPaymentIntent());

            return ResponseEntity.ok(refundResponse);
        } catch (StripeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/get_intents")
    public ResponseEntity<List<PaymentIntentResponse>> getIntents() {
        try {
            PaymentIntentListParams params = PaymentIntentListParams.builder().setLimit(3L).build();
            PaymentIntentCollection paymentIntents = PaymentIntent.list(params);

            List<PaymentIntentResponse> intentDTOs = paymentIntents.getData().stream().map(intent -> {
            	PaymentIntentResponse dto = new PaymentIntentResponse();
                dto.setId(intent.getId());
                dto.setAmount(intent.getAmount());
                dto.setCurrency(intent.getCurrency());
                dto.setStatus(intent.getStatus());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(intentDTOs);
        } catch (StripeException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
