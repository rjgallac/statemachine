package com.example.demo;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

	@Autowired
	private StateMachineFactory<ApplicationReviewStates, ApplicationReviewEvents> stateMachineFactory;



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println("Hello World from Application Runner");
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		StateMachine<ApplicationReviewStates, ApplicationReviewEvents> stateMachine = stateMachineFactory.getStateMachine(uuid);

		stateMachine.startReactively();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.SUBMIT).build())).subscribe();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.REVIEWED).build())).subscribe();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.CLOSE).build())).subscribe();
		stateMachine.stopReactively();

		System.out.println("------------------------");
		UUID uuid1 = UUID.randomUUID();
		System.out.println(uuid1);
		stateMachine = stateMachineFactory.getStateMachine(uuid1);
		stateMachine.stopReactively();
		stateMachine.getStateMachineAccessor().doWithAllRegions(sma ->
			sma.resetStateMachineReactively(new DefaultStateMachineContext<>(ApplicationReviewStates.PEER_REVIEW, null, null, null)).subscribe());

		stateMachine.startReactively();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.REVIEWED).build())).subscribe();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.APPROVE).build())).subscribe();
		stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ApplicationReviewEvents.CLOSE).build())).subscribe();

	}



}
