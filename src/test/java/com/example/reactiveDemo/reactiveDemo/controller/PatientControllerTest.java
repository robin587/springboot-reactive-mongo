package com.example.reactiveDemo.reactiveDemo.controller;

import com.example.reactiveDemo.reactiveDemo.document.Patient;
import com.example.reactiveDemo.reactiveDemo.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class PatientControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PatientRepository patientRepository;

    List<Patient> patientList = Arrays.asList( new Patient(null,"Kelly","Aller","toronto"),
            new Patient(null,"John","Silver","toronto"),
            new Patient(null,"Andy","Mark","toronto"),
            new Patient("123","Allan","Donald","toronto"));

    @BeforeEach
    void setUp() {

        patientRepository.deleteAll()
                .thenMany(Flux.fromIterable(patientList))
                .flatMap(patient ->  patientRepository.save(patient))
                .doOnNext(patient -> System.out.println("Patient saved :"+patient))
                .blockLast();
    }

    @Test
    void getAllPatient() {

        Flux<Patient> responseBody = webTestClient.get().uri("/v1/getAllPatient")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Patient.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getAllPatient_SecondTest() {

        webTestClient.get().uri("/v1/getAllPatient")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Patient.class)
                .consumeWith(patientList -> patientList.getResponseBody().stream().forEach(p -> assertThat(p.getId()).isNotNull()));
      }

    @Test
    void getPatient_whenGivenId() {

        Patient actualPatient = new Patient("123","Allan","Donald","toronto");
        Flux<Patient> responseBody = webTestClient.get().uri("/v1/getPatient/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Patient.class)
                .getResponseBody();

        StepVerifier.create(responseBody.log())
                .expectSubscription()
                .expectNext(actualPatient)
                .verifyComplete();
    }

    @Test
    void updatePatient_givenId(){
        String updatedAddress="Quebec";
        Patient actualPatient = new Patient("123","Allan","Donald",updatedAddress);

        webTestClient.put().uri("/v1/updateItem/{id}","123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(actualPatient),Patient.class)
                .exchange()
                .expectBody()
                .jsonPath("$.address").isEqualTo("toronto");
    }
}