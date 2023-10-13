package com.example.demo.Service;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateMachineService {

    private final StateMachineFactory<ApplicationReviewStates, ApplicationReviewEvents> stateMachineFactory;

    public StateMachine<ApplicationReviewStates, ApplicationReviewEvents> getMachine(long id, ApplicationReviewStates applicationReviewStates) {
        StateMachine<ApplicationReviewStates, ApplicationReviewEvents> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stopReactively();
        stateMachine.getStateMachineAccessor().doWithAllRegions(sma ->
                sma.resetStateMachineReactively(new DefaultStateMachineContext<>(applicationReviewStates, null, null, null)).subscribe());

        stateMachine.startReactively();
        return stateMachine;
    }
}
