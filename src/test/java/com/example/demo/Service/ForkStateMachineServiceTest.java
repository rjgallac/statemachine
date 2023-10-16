package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ForkStateMachineServiceTest {
    @Autowired
    private ForkStateMachineService forkStateMachineService;

    @Test
    public void test(){
        ApplicationReviewStates id;

        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = forkStateMachineService.getMachineDefault();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.SUBMIT).build())).subscribe();
        id = machine.getState().getId();
        assertEquals(ApplicationReviewStates.PRINCIPAL_REVIEW, id);
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
        id = machine.getState().getId();
        assertEquals(ApplicationReviewStates.END, id);
    }
}