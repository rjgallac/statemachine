package com.example.demo;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

	@Autowired
	private StateMachine<ApplicationReviewStates, ApplicationReviewEvents> stateMachine;



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println("Hello World from Application Runner");

		stateMachine.startReactively();
		stateMachine.sendEvent(ApplicationReviewEvents.SUBMIT);
		stateMachine.sendEvent(ApplicationReviewEvents.REVIEWED);
		stateMachine.sendEvent(ApplicationReviewEvents.APPROVE);
//		stateMachine.sendEvent(ApplicationReviewEvents.CLOSE);
//		stateMachine.stopReactively();

		System.out.println("------------------------");
//		stateMachine.startReactively();
		stateMachine.getStateMachineAccessor().doWithAllRegions(sma ->
			sma.resetStateMachineReactively(new DefaultStateMachineContext<>(ApplicationReviewStates.PEER_REVIEW, null, null, null))
		);
		stateMachine.sendEvent(ApplicationReviewEvents.REVIEWED);
		stateMachine.sendEvent(ApplicationReviewEvents.APPROVE);
		stateMachine.sendEvent(ApplicationReviewEvents.CLOSE);

	}



}
