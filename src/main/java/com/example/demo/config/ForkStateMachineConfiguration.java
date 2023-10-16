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
@EnableStateMachineFactory(name = "fork")
public class ForkStateMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {

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
                .choice(ApplicationReviewStates.PRINCIPAL_REVIEW)
                .states(EnumSet.allOf(ApplicationReviewStates.class));

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions
            .withChoice()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW)
                .first(ApplicationReviewStates.APPROVED, approveGuard())
                .then(ApplicationReviewStates.REJECTED, rejectGuard())
            .and()
            .withExternal()
                .source(ApplicationReviewStates.START).target(ApplicationReviewStates.PRINCIPAL_REVIEW).event(ApplicationReviewEvents.SUBMIT);
    }

    @Bean
    public Guard<ApplicationReviewStates, ApplicationReviewEvents> approveGuard() {
        return new Guard<ApplicationReviewStates, ApplicationReviewEvents>() {

            @Override
            public boolean evaluate(StateContext<ApplicationReviewStates, ApplicationReviewEvents> context) {
                return true;
            }
        };
    }
    @Bean
    public Guard<ApplicationReviewStates, ApplicationReviewEvents> rejectGuard() {
        return new Guard<ApplicationReviewStates, ApplicationReviewEvents>() {

            @Override
            public boolean evaluate(StateContext<ApplicationReviewStates, ApplicationReviewEvents> context) {
                return false;
            }
        };
    }


}