package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableStateMachineFactory(name = "parent")
public class ParentChildMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {

    @Autowired
    private StateMachineListener stateMachineListener;

    @Override
    public void configure(StateMachineConfigurationConfigurer<ApplicationReviewStates, ApplicationReviewEvents> config) throws Exception {
        config
            .withConfiguration()
            .autoStartup(true)
            .stateDoActionPolicyTimeout(10, TimeUnit.SECONDS)
            .listener(stateMachineListener);
    }

    @Override
    public void configure(StateMachineStateConfigurer<ApplicationReviewStates, ApplicationReviewEvents> states) throws Exception {
        states
            .withStates()
                .initial(ApplicationReviewStates.START)
                .state(ApplicationReviewStates.PEER_REVIEW)
                .state(ApplicationReviewStates.END)
                .and()
                .withStates()
                    .parent(ApplicationReviewStates.PEER_REVIEW)
                    .initial(ApplicationReviewStates.S2I)
                    .state(ApplicationReviewStates.APPROVED)
                    .end(ApplicationReviewStates.END)
                    .and()
                .withStates()
                    .parent(ApplicationReviewStates.PEER_REVIEW)
                    .initial(ApplicationReviewStates.S3I)
                    .state(ApplicationReviewStates.REJECTED)
                    .end(ApplicationReviewStates.END2);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ApplicationReviewStates.START).target(ApplicationReviewStates.PEER_REVIEW).event(ApplicationReviewEvents.SUBMIT)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.APPROVED).event(ApplicationReviewEvents.APPROVE)
            .and()
            .withExternal()
                .source(ApplicationReviewStates.APPROVED).target(ApplicationReviewStates.END).event(ApplicationReviewEvents.CLOSE);
    }

}