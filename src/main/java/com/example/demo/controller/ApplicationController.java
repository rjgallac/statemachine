package com.example.demo.controller;

import com.example.demo.Service.ApplicationService;
import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import com.example.demo.model.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;


    @GetMapping
    public ResponseEntity<List<Application>> get(){
        return ResponseEntity.ok(applicationService.getAll());
    }

    @PostMapping
    public void create() {
        Application application = Application.builder()
                .applicationReviewStates(ApplicationReviewStates.START)
                .date(new Date())
                .build();
        applicationService.save(application);

    }

    @PutMapping("/{id}/{state}")
    public void updateState(@PathVariable Long id, @PathVariable String state ) {
        applicationService.updateState(id, state);
    }

}
