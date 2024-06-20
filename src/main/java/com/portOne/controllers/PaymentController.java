package com.portOne.controllers;

import com.portOne.models.PaymentIntentResponse;
import com.portOne.models.RefundResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCaptureParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

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
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

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

            if (!"requires_capture".equals(paymentIntent.getStatus())) {
                return ResponseEntity.status(400)
                        .body("PaymentIntent must be in requires_capture status to be captured. Current status: "
                                + paymentIntent.getStatus());
            }

            PaymentIntentCaptureParams params = PaymentIntentCaptureParams.builder().build();
            PaymentIntent capturedIntent = paymentIntent.capture(params);

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

            Refund refund = Refund.create(params);

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
