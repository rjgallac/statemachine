package com.example.demo;

import com.example.demo.config.ApplicationReviewEvents;
import com.example.demo.config.ApplicationReviewStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

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

		stateMachine.start();
		stateMachine.sendEvent(ApplicationReviewEvents.SUBMIT);


		System.out.println(stateMachine.getState());

		stateMachine.sendEvent(ApplicationReviewEvents.REVIEWED);


		System.out.println(stateMachine.getState());

		stateMachine.sendEvent(ApplicationReviewEvents.APPROVE);

		System.out.println(stateMachine.getState());


	}



}
