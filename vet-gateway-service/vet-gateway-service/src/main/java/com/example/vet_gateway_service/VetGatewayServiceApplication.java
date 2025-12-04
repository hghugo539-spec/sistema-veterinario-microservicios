package com.example.vet_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VetGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetGatewayServiceApplication.class, args);
	}

}
