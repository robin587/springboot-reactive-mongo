package com.example.reactiveDemo.reactiveDemo.repository;

import com.example.reactiveDemo.reactiveDemo.document.Patient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public  interface PatientRepository extends ReactiveMongoRepository<Patient, String> {
}
