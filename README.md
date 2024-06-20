# Stripe Payment Gateway Integration Application

This Spring Boot application demonstrates integration with the Stripe Payment Gateway to handle Payment Intents and Refunds.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Deployment Link](#deployment-link)
- [Video Explanation](#video-explanation)
- [Postman Collection](#postman-collection)
- [Reference](#reference)


## Introduction

This application integrates with the Stripe Payment Gateway API to perform various operations such as creating Payment Intents, confirming/capturing Payment Intents, creating refunds, and listing Payment Intents.

## Features

- Create a new Payment Intent with customizable parameters.
- Confirm a Payment Intent using a payment method and return URL.
- Capture a Payment Intent that requires manual capture.
- Create refunds for Payment Intents.
- Retrieve a list of Payment Intents.

## Technologies Used

- Java
- Spring Boot
- Stripe API
- Maven


## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- Java Development Kit (JDK) 17
- Maven
- Stripe API key (test mode) 

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Kapil7982/PortOne.git
   cd Stripe
   ```

## Configuration
Set your Stripe API key in StripeConfig.java.

## Running the Application
The application will start on port 8080 by default.

## API Endpoints

### Create Payment Intent

- **POST** `/api/v1/create_intent`

  Creates a new Payment Intent with default parameters.
  
![createIntent](https://github.com/Kapil7982/StripePaymentGetway/assets/103938868/ba97a693-fb7c-448c-8ba3-bdab0f3693a0)
-

### Confirm Payment Intent

- **POST** `/api/v1/confirm_intent/{id}`

  Confirms a Payment Intent by ID.
  
![confirmIntent](https://github.com/Kapil7982/StripePaymentGetway/assets/103938868/15fa347f-09d3-457c-a997-3d45dbae1cde)
-
### Capture Payment Intent

- **POST** `/api/v1/capture_intent/{id}`
  
  Captures a Payment Intent that requires manual capture.

![captureIntent](https://github.com/Kapil7982/StripePaymentGetway/assets/103938868/bc3d84d5-4a30-4967-b71d-9bd0da47a321)
-
### Create Refund

- **POST** `/api/v1/create_refund/{id}`
  
  Creates a refund for a Payment Intent by ID.

![createRefund](https://github.com/Kapil7982/StripePaymentGetway/assets/103938868/be2cb317-5d99-4200-9c69-fa3f4200fdef)
-
 
### Get Payment Intents

- **GET** `/api/v1/get_intents`
  Retrieves a list of Payment Intents.

![getAllIntent](https://github.com/Kapil7982/StripePaymentGetway/assets/103938868/3c457748-9f23-4bee-ab3a-82817553ae92)
-

## Deployment Link
  Link:- https://stripepaymentgetway.onrender.com
 -

## Video Explanation
  Link:- https://drive.google.com/file/d/1jpPQDqVKZGXjd3MrfZ6v8rnmG44sUKdy/view?usp=sharing
  -

## Postman Collection
  Link:- [https://martian-water-219147.postman.co/workspace/PortOne-Workspace~bf4682ea-d2d9-4615-bbf0-e24f618e3e4d/collection/24278763-f3398bc4-a631-434b-94d8-4935c5366011?action=share&creator=24278763](https://www.postman.com/portone-2987/workspace/portone/collection/24278763-f3398bc4-a631-434b-94d8-4935c5366011?action=share&creator=24278763)
  -
## Reference

- Stripe API docs - https://stripe.com/docs/api/payment_intents 
- Payment Intents - https://stripe.com/docs/payments/payment-intents
- Stripe go-SDK - https://github.com/stripe/stripe-go
- Stripe Other Language SDKs - https://github.com/stripe
- Setup simple backend in Golang - https://github.com/gorilla/mux

