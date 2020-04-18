package com.example.reactiveDemo.reactiveDemo.handler;

import com.example.reactiveDemo.reactiveDemo.document.Patient;
import com.example.reactiveDemo.reactiveDemo.repository.PatientRepository;
import com.mongodb.MongoNodeIsRecoveringException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PatientHandler {

    @Autowired
    private PatientRepository patientRepository;

    public Mono<ServerResponse> getPatient(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientRepository.findById(id), Patient.class);
    }

}
