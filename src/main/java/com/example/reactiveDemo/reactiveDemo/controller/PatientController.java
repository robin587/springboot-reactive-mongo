package com.example.reactiveDemo.reactiveDemo.controller;

import com.example.reactiveDemo.reactiveDemo.document.Patient;
import com.example.reactiveDemo.reactiveDemo.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Slf4j
public class PatientController {

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private PatientRepository patientRepository;

    @GetMapping("/v1/getAllPatient")
    public Flux<Patient> getAllPatients(){

        return patientRepository.findAll();
    }

    @PutMapping("/v1/updateItem/{id}")
    public Mono<ResponseEntity> updatePatient(@PathVariable("id") String id, @RequestBody Patient patient){

        return patientRepository.findById(id)
                .map(savedPatient -> {
                    savedPatient.setAddress(patient.getAddress());
                    savedPatient.setFirstName(patient.getFirstName());
                    savedPatient.setLastName(patient.getLastName());
                    patientRepository.save(savedPatient);
                    return new ResponseEntity(savedPatient, HttpStatus.OK);
                })
                .defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/v1/getFluxData",produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Integer> getFluxData(){
        return Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/v2/getFluxData",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getFulxData(){
        return Flux.range(1,50)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

}
