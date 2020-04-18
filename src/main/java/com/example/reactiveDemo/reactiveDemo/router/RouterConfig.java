package com.example.reactiveDemo.reactiveDemo.router;

import com.example.reactiveDemo.reactiveDemo.handler.PatientHandler;
import com.example.reactiveDemo.reactiveDemo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeConfig(PatientHandler patientHandler){
        return route(GET("/v1/getPatient/{id}"),patientHandler::getPatient);
    }
}
