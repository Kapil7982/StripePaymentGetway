package com.portOne.config;

import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51PTJpPHbgWIRtNpzTasGNIMDnv4Vnm2VP8FWYXADEzKloyaRyP2f6Sec5RZvfkuCUACUBFkvOuUiYrTwCHy5qWZY00RQMFYLzi";
    }
}