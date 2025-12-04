package com.example.vet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // Para registrarse en Eureka
@EnableFeignClients    // Para poder llamar al Users Service
public class VetApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetApplication.class, args);
    }

}