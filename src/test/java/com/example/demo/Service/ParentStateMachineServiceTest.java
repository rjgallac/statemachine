package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.hibernate.annotations.Parent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParentStateMachineServiceTest {

    @Autowired
    private ParentStateMachineService parentStateMachineService;

    @Test
    public void test(){
        ApplicationReviewStates id;

        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> machine = parentStateMachineService.getMachineDefault();
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.SUBMIT).build())).subscribe();
        id = machine.getState().getId();
        assertEquals(ApplicationReviewStates.PEER_REVIEW, id);
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
        Collection<ApplicationReviewStates> ids = machine.getState().getIds();
        System.out.println(ids);
//        assertEquals(ApplicationReviewStates.APPROVED, id);
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.CLOSE).build())).subscribe();
        Collection<ApplicationReviewStates> ids1 = machine.getState().getIds();
        System.out.println(ids1);
        assertEquals(ApplicationReviewStates.END, ids1.toArray()[0]);
    }
}