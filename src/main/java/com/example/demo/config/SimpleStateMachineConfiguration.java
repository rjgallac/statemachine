package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableStateMachineFactory
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {

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
            .end(ApplicationReviewStates.END)
            .states(new HashSet<ApplicationReviewStates>(Arrays.asList(ApplicationReviewStates.PEER_REVIEW, ApplicationReviewStates.PRINCIPAL_REVIEW, ApplicationReviewStates.APPROVED, ApplicationReviewStates.APPROVED)));
//                .state(ApplicationReviewStates.PEER_REVIEW, initAction(), errorAction())
//                .state(ApplicationReviewStates.PRINCIPAL_REVIEW, initAction(), errorAction());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ApplicationReviewStates.START).target(ApplicationReviewStates.PEER_REVIEW).event(ApplicationReviewEvents.SUBMIT)
                .action(initAction())
                .and()
            .withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.PRINCIPAL_REVIEW).event(ApplicationReviewEvents.REVIEWED)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.APPROVED).event(ApplicationReviewEvents.APPROVE)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.APPROVED).target(ApplicationReviewStates.END).event(ApplicationReviewEvents.CLOSE)
                .and()
            .withExternal()
                .source(ApplicationReviewStates.REJECTED).target(ApplicationReviewStates.END).event(ApplicationReviewEvents.CLOSE);
    }

    @Bean
    public Action<ApplicationReviewStates, ApplicationReviewEvents> initAction() {
        return ctx ->
            System.out.println(ctx.getTarget().getId());
    }

    @Bean
    public Action<ApplicationReviewStates, ApplicationReviewEvents> errorAction() {
        return ctx -> System.out.println(
                "Error " + ctx.getSource().getId() + ctx.getException());
    }



}