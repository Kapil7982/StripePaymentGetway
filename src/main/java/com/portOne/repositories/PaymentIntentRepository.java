package com.portOne.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portOne.models.PaymentIntentEntity;

@Repository
public interface PaymentIntentRepository extends JpaRepository<PaymentIntentEntity, Long> {

	PaymentIntentEntity findByPaymentIntentId(String id);
	
}