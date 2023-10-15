package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ContextConfiguration(classes = {StateMachineFactory.class, StateMachineService.class})
public class StateMachineServiceTest {

    @Autowired
    private StateMachineService stateMachineService;

    @Test
    public void test(){
        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = stateMachineService.getMachineDefault();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.SUBMIT).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.REVIEWED).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.CLOSE).build())).subscribe();
        ApplicationReviewStates id = machine.getState().getId();
        assertEquals(ApplicationReviewStates.END, id);
    }

}