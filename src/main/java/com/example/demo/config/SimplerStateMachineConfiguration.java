package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableStateMachineFactory(name = "two")
public class SimplerStateMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {

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
                .end(ApplicationReviewStates.END);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ApplicationReviewStates.START).target(ApplicationReviewStates.PEER_REVIEW).event(ApplicationReviewEvents.SUBMIT)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.END).event(ApplicationReviewEvents.CLOSE);
    }



}